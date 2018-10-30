package com.liansen.flow.rest.task.resource;

import com.liansen.common.constant.CoreConstant;
import com.liansen.common.model.Authentication;
import com.liansen.common.model.ExecuteStatus;
import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.rest.phpClient.PhpService;
import com.liansen.flow.rest.phpClient.repository.PhpUserTaskRepository;
import com.liansen.flow.rest.phpClient.repository.UserGroupRepository;
import com.liansen.flow.rest.phpClient.request.PhpUserTaskRequest;
import com.liansen.flow.rest.task.TaskCompleteRequest;
import com.liansen.flow.rest.task.TaskResponse;
import com.liansen.flow.rest.variable.RestVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.api.User;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.flowable.bpmn.model.Process;

import java.util.*;

/**
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "任务完成接口")
@RestController
public class TaskCompleteResource extends BaseTaskResource {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    PhpService phpService;

    @Autowired
    PhpUserTaskRepository phpUserTaskRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @ApiOperation("任务完成")
    @PutMapping(value = "/tasks/{taskId}/complete",name = "任务完成")
    @ResponseStatus(value = HttpStatus.OK)
    public void completeTask(@PathVariable String taskId, @RequestBody(required = false)TaskCompleteRequest taskCompleteRequest) throws Exception {

        Task task = getTaskFromRequest(taskId);
        if(task.getAssignee() == null) {
            taskService.setAssignee(taskId, Authentication.getUserId());
        }

        Map<String,Object> completeVariables = new HashMap<String,Object>();
        if(taskCompleteRequest != null && taskCompleteRequest.getVariables() != null) {
            for(RestVariable variable : taskCompleteRequest.getVariables()) {
                if(variable.getName() == null) {
                    exceptionFactory.throwIllegalArgument(ErrorConstant.PARAM_NOT_FOUND,"变量名称");
                }
                completeVariables.put(variable.getName(),restResponseFactory.getVariableValue(variable));
            }
        }
        if(task.getDelegationState() != null && task.getDelegationState().equals(DelegationState.PENDING)) {
            if(completeVariables.isEmpty()) {
                taskService.resolveTask(taskId);
            }else {
                taskService.resolveTask(taskId,completeVariables);

            }
        }else {
            if(completeVariables.isEmpty()) {
                try{
                    taskService.complete(taskId);
                }catch (Exception e){
                }
            }else {
                if(completeVariables.get("submitType").equals("n")){
                    // 查找所有并行任务节点，同时驳回
                    List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(
                            taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());
                    for (Task t : taskList) {
                        taskService.complete(t.getId(),completeVariables);
                    }
                }else{
                    BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                    Process process = bpmnModel.getProcesses().get(0);
                    Collection<UserTask> flowElements = process.findFlowElementsOfType(UserTask.class);
                    for (UserTask userTask : flowElements) {
                        List<String> listCandidateUsers = new ArrayList<String>();
                        List<String> listCandidateGroups = userTask.getCandidateGroups();
                        listCandidateUsers = userTask.getCandidateUsers();
                        if(listCandidateGroups.size() != 0){
                            for(String groups : listCandidateGroups){
                                 listCandidateUsers = userGroupRepository.getUserGroup(Integer.valueOf(groups));
                            }
                        }
                        if(listCandidateUsers.size() != 0){
                            completeVariables.put(userTask.getName(),listCandidateUsers);
                        }
                    }
                    TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
                    System.out.println(tokenUserIdUtils.tokenUserId());
                    //token失效
                    if(tokenUserIdUtils == null || tokenUserIdUtils.tokenUserId() == null){
                        exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_NOT_FOUND);
                    }
                    PhpUserTaskRequest phpUserTaskRequest = phpUserTaskRepository.findByUserIdAndTaskId(tokenUserIdUtils.tokenUserId(),taskId);
                    if(phpUserTaskRequest != null){
                        phpService.modify(true,phpUserTaskRequest.getPhpTaskId());
                        taskService.complete(taskId,completeVariables);
                    }
                }
            }
        }

        loggerConverter.save("完成了任务 '" + task.getName() + "'");
    }

    // 查询运行时的任务
    @GetMapping(value = "/runTasks/{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void runTask(@PathVariable String taskId){
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        for(Task t : tasks){
            if(t.getName().equals(task.getName())){
                return;
            }
        }
        if(tasks.size() == 0){
            return;
        }
        PhpServiceForTask(tasks);
    }

    /**
     * 根据流程实例ID和任务key值查询所有同级任务集合
     *
     * @param processInstanceId
     * @param key
     * @return
     */
    private List<Task> findTaskListByKey(String processInstanceId, String key) {
        return taskService.createTaskQuery().processInstanceId(
                processInstanceId).taskDefinitionKey(key).list();
    }

    /**
     * 根据任务ID获取对应的流程实例
     *
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private ProcessInstance findProcessInstanceByTaskId(String taskId)
            throws Exception {
        // 找到流程实例
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery().processInstanceId(
                        findTaskById(taskId).getProcessInstanceId())
                .singleResult();
        if (processInstance == null) {
            throw new Exception("流程实例未找到!");
        }
        return processInstance;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(
                taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }

    public void PhpServiceForTask(List<Task> tasks){
        for(Task task :tasks){
            if(task.getAssignee() != null){
                Map<String,String> map = new HashMap<>();
                map.put("user",task.getAssignee());
                PhpServiceTask(map,task);
            }else{
                List<IdentityLink> identityLink =taskService.getIdentityLinksForTask(task.getId());
                for(IdentityLink identity:identityLink){
                    if(identity.getUserId() != null){
                        Map<String,String> map = new HashMap<>();
                        map.put("user",identity.getUserId());
                        PhpServiceTask(map,task);
                    }if(identity.getGroupId() != null){
                        Map<String,String> map = new HashMap<>();
                        map.put("group",identity.getGroupId());
                        PhpServiceTask(map,task);
                    }
                }
            }
        }
    }

    public void PhpServiceTask(Map<String,String> map,Task tasks){
        List<String> userId = new ArrayList<>();
        if(map.get("group") != null){
            userId = userGroupRepository.getUserGroup(Integer.valueOf(map.get("group")));
        }else{
            userId.add(map.get("user"));
        }
        for(String id :userId){
            User user = identityService.createUserQuery().userId(id.toString()).singleResult();
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setAssignee(user.getFirstName());
            taskResponse.setCreateTime(tasks.getCreateTime());
            taskResponse.setId(tasks.getId());
            taskResponse.setDueDate(tasks.getDueDate());
            taskResponse.setName(tasks.getName());
            String json =  phpService.phpTaskService(taskResponse);

            JSONObject jsonObject = JSONObject.fromObject(json);
            ExecuteStatus status = (ExecuteStatus)JSONObject.toBean(jsonObject, ExecuteStatus.class);
            if(status.getResult() == 0){
                PhpUserTaskRequest phpTask = new PhpUserTaskRequest();
                phpTask.setTaskId(tasks.getId());
                phpTask.setPhpTaskId(status.getId());
                phpTask.setUserId(id);
                phpUserTaskRepository.save(phpTask);
            }

        }
    }

}






























