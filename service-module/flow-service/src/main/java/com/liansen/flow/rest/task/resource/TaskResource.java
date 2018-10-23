package com.liansen.flow.rest.task.resource;


import com.liansen.common.constant.CoreConstant;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.constant.TableConstant;
import com.liansen.flow.rest.phpClient.PhpService;
import com.liansen.flow.rest.phpClient.repository.PhpTaskAndTaskRepository;
import com.liansen.flow.rest.phpClient.repository.PhpTaskRepository;
import com.liansen.flow.rest.phpClient.request.PhpTaskIdAndTaskId;
import com.liansen.flow.rest.task.TaskDetailResponse;
import com.liansen.flow.rest.task.TaskEditRequest;
import com.liansen.flow.rest.task.TaskPaginateList;
import com.liansen.flow.rest.task.TaskResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.HistoricTaskInstanceQueryProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "任务基础接口")
@RestController
public class TaskResource extends BaseTaskResource {

    @Autowired
    PhpTaskAndTaskRepository phpTaskAndTaskRepository;

    @Autowired
    PhpService phpService;
    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

    static {
        allowedSortProperties.put("deleteReason", HistoricTaskInstanceQueryProperty.DELETE_REASON);
        allowedSortProperties.put("duration", HistoricTaskInstanceQueryProperty.DURATION);
        allowedSortProperties.put("endTime", HistoricTaskInstanceQueryProperty.END);
        allowedSortProperties.put("executionId", HistoricTaskInstanceQueryProperty.EXECUTION_ID);
        allowedSortProperties.put("taskInstanceId", HistoricTaskInstanceQueryProperty.HISTORIC_TASK_INSTANCE_ID);
        allowedSortProperties.put("processDefinitionId", HistoricTaskInstanceQueryProperty.PROCESS_DEFINITION_ID);
        allowedSortProperties.put("processInstanceId", HistoricTaskInstanceQueryProperty.PROCESS_INSTANCE_ID);
        allowedSortProperties.put("assignee", HistoricTaskInstanceQueryProperty.TASK_ASSIGNEE);
        allowedSortProperties.put("taskDefinitionKey", HistoricTaskInstanceQueryProperty.TASK_DEFINITION_KEY);
        allowedSortProperties.put("description", HistoricTaskInstanceQueryProperty.TASK_DESCRIPTION);
        allowedSortProperties.put("dueDate", HistoricTaskInstanceQueryProperty.TASK_DUE_DATE);
        allowedSortProperties.put("name", HistoricTaskInstanceQueryProperty.TASK_NAME);
        allowedSortProperties.put("owner", HistoricTaskInstanceQueryProperty.TASK_OWNER);
        allowedSortProperties.put("priority", HistoricTaskInstanceQueryProperty.TASK_PRIORITY);
        allowedSortProperties.put("tenantId", HistoricTaskInstanceQueryProperty.TENANT_ID_);
        allowedSortProperties.put("startTime", HistoricTaskInstanceQueryProperty.START);
    }

    @ApiOperation("任务查询")
    //单人任务(负责人)
    @GetMapping(value = "/tasks", name = "单人任务查询")
    public PageResponse getTasks(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
        //token失效
        if(tokenUserIdUtils == null || tokenUserIdUtils.tokenUserId() == null){
            exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_NOT_FOUND);
        }

        if(!tokenUserIdUtils.tokenUserId().equals(TableConstant.ADMIN_USER_ID)){
            requestParams.put("taskAssignee",tokenUserIdUtils.tokenUserId());
        }
       return getTask(requestParams);
    }

    @ApiOperation("根据任务ID查询")
    @GetMapping(value = "/tasks/{taskId}", name = "根据ID任务查询")
    public TaskDetailResponse getTaskById(@PathVariable("taskId") String taskId) {
        Task task = null;
        HistoricTaskInstance historicTaskInstance = getHistoricTaskFromRequest(taskId);
        if (historicTaskInstance.getEndTime() == null) {
            task = getTaskFromRequest(taskId);
        }
        return restResponseFactory.createTaskDetailResponse(historicTaskInstance, task);
    }

    @ApiOperation("修改任务")
    @PutMapping(value = "/tasks/{taskId}", name = "任务修改")
    @Transactional
    public TaskResponse updateTask(@PathVariable String taskId, @RequestBody TaskEditRequest taskEditRequest) {
        Task task = getTaskFromRequest(taskId);
        task.setName(taskEditRequest.getName());
        task.setDescription(taskEditRequest.getDescription());
        task.setAssignee(taskEditRequest.getAssignee());
        task.setOwner(taskEditRequest.getOwner());
        task.setDueDate(taskEditRequest.getDueDate());
        task.setPriority(taskEditRequest.getPriority());
        task.setCategory(taskEditRequest.getCategory());
        taskService.saveTask(task);
        loggerConverter.save("修改了任务 '" + task.getName() + "'");
        return restResponseFactory.createTaskResponse(task);
    }

    @ApiOperation("删除任务")
    @DeleteMapping(value = "/tasks/{taskId}", name = "任务删除")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteTask(@PathVariable String taskId) {
        HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
        if (task.getEndTime() == null) {
            exceptionFactory.throwForbidden(ErrorConstant.TASK_RUN_NOT_DELETE, taskId);
        }
        List<PhpTaskIdAndTaskId> phpTaskIdAndTaskId = phpTaskAndTaskRepository.findByTaskId(taskId);
        for(PhpTaskIdAndTaskId phpTask:phpTaskIdAndTaskId){
            if(phpTask != null){
                try{
                    phpService.deletePhpTask(phpTask.getPhpTaskId());
                }catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
        }
        phpTaskAndTaskRepository.deleteByTaskId(taskId);
        historyService.deleteHistoricTaskInstance(task.getId());
        loggerConverter.save("删除了任务 '"+ task.getName() +"'");
    }

    public HistoricTaskInstance findTask(@PathVariable String taskId){

        HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
        if (task.getEndTime() == null) {
            exceptionFactory.throwForbidden(ErrorConstant.TASK_RUN_NOT_DELETE, taskId);
        }
        return task;
    }


    private  PageResponse getTask(@RequestParam Map<String, String> requestParams) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();

        if (ObjectUtils.isNotEmpty(requestParams.get("taskId"))) {
            query.taskId(requestParams.get("taskId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
            query.processInstanceId(requestParams.get("processInstanceId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceBusinessKey"))) {
            query.processInstanceBusinessKeyLike(ObjectUtils.convertToLike(requestParams.get("processInstanceBusinessKey")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionKey"))) {
            query.processDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("processDefinitionKey")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionId"))) {
            query.processDefinitionId(requestParams.get("processDefinitionId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionName"))) {
            query.processDefinitionNameLike(ObjectUtils.convertToLike(requestParams.get("processDefinitionName")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("executionId"))) {
            query.executionId(requestParams.get("executionId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskName"))) {
            query.taskNameLike(ObjectUtils.convertToLike(requestParams.get("taskName")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskDescription"))) {
            query.taskDescriptionLike(ObjectUtils.convertToLike(requestParams.get("taskDescription")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskDefinitionKey"))) {
            query.taskDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("taskDefinitionKey")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskAssignee"))) {
            query.taskAssignee(requestParams.get("taskAssignee"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskOwner"))) {
            query.taskOwner(requestParams.get("taskOwner"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskInvolvedUser"))) {
            query.taskInvolvedUser(requestParams.get("taskInvolvedUser"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskPriority"))) {
            query.taskPriority(ObjectUtils.convertToInteger(requestParams.get("taskPriority")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("finished"))) {
            boolean isFinished = ObjectUtils.convertToBoolean(requestParams.get("finished"));
            if (isFinished) {
                query.finished();
            } else {
                query.unfinished();
            }
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processFinished"))) {
            boolean isProcessFinished = ObjectUtils.convertToBoolean(requestParams.get("processFinished"));
            if (isProcessFinished) {
                query.processFinished();
            } else {
                query.processUnfinished();
            }
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("parentTaskId"))) {
            query.taskParentTaskId(requestParams.get("parentTaskId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("dueDateAfter"))) {
            query.taskDueAfter(ObjectUtils.convertToDatetime(requestParams.get("dueDateAfter")));
        }

        if (ObjectUtils.isNotEmpty(requestParams.get("dueDateBefore"))) {
            query.taskDueBefore(ObjectUtils.convertToDatetime(requestParams.get("dueDateBefore")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCreatedBefore"))) {
            query.taskCreatedBefore(ObjectUtils.convertToDatetime(requestParams.get("taskCreatedBefore")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCreatedAfter"))) {
            query.taskCreatedAfter(ObjectUtils.convertToDatetime(requestParams.get("taskCreatedAfter")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCompletedBefore"))) {
            query.taskCompletedBefore(ObjectUtils.convertToDatetime(requestParams.get("taskCompletedBefore")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCompletedAfter"))) {
            query.taskCompletedAfter(ObjectUtils.convertToDatetime(requestParams.get("taskCompletedAfter")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
            query.taskTenantId(requestParams.get("tenantId"));
        }

        if (ObjectUtils.isNotEmpty(requestParams.get("taskCandidateUser"))) {
            query.taskCandidateUser(requestParams.get("taskCandidateUser"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCandidateGroup"))) {
            query.taskCandidateGroup(requestParams.get("taskCandidateGroup"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCandidateGroups"))) {
            String[] candidateGroups = requestParams.get("taskCandidateGroups").split(",");
            List<String> groups = new ArrayList<String>(candidateGroups.length);
            for (String candidateGroup : candidateGroups) {
                groups.add(candidateGroup);
            }
            query.taskCandidateGroupIn(groups);
        }
//        query.unfinished();
        return new TaskPaginateList(restResponseFactory).paginateList(getPageable(requestParams), query, allowedSortProperties);
    }
}
