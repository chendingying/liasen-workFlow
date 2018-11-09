package com.liansen.flow.rest.instance.resource;

import com.liansen.common.model.Authentication;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.rest.instance.ProcessInstanceDetailResponse;
import com.liansen.flow.rest.instance.ProcessInstancePaginateList;
import com.liansen.flow.rest.instance.ProcessInstanceStartRequest;
import com.liansen.flow.rest.instance.ProcessInstanceStartResponse;
import com.liansen.flow.rest.phpClient.PhpService;
import com.liansen.flow.rest.phpClient.repository.PhpUserTaskRepository;
import com.liansen.flow.rest.phpClient.repository.UserGroupRepository;
import com.liansen.flow.rest.phpClient.request.PhpUserTaskRequest;
import com.liansen.flow.rest.readTask.TaskReadService;
import com.liansen.flow.rest.task.domain.HistoricTaskDoMain;
import com.liansen.flow.rest.task.repository.TaskRepository;
import com.liansen.flow.rest.task.resource.TaskAsync;
import com.liansen.flow.rest.task.resource.TaskCompleteResource;
import com.liansen.flow.rest.variable.RestVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.HistoricProcessInstanceQueryProperty;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.testng.annotations.Test;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 流程实例接口
 * 
 * @author wengwh
 * @date 2018年4月23日
 */
@Api(description = "流程实例基础接口")
@RestController
public class ProcessInstanceResource extends BaseProcessInstanceResource {
	@Autowired
	IdentityService identityService;

    @Autowired
    TaskAsync taskAsync;

	@Autowired
	PhpService phpService;
	@Autowired
	TaskCompleteResource taskCompleteResource;

	@Autowired
	PhpUserTaskRepository phpUserTaskRepository;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	TaskReadService taskReadService;

	@Autowired
	TaskRepository taskRepository;
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("id", HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
		allowedSortProperties.put("processDefinitionId", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("processDefinitionKey", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
		allowedSortProperties.put("businessKey", HistoricProcessInstanceQueryProperty.BUSINESS_KEY);
		allowedSortProperties.put("startTime", HistoricProcessInstanceQueryProperty.START_TIME);
		allowedSortProperties.put("endTime", HistoricProcessInstanceQueryProperty.END_TIME);
		allowedSortProperties.put("duration", HistoricProcessInstanceQueryProperty.DURATION);
		allowedSortProperties.put("tenantId", HistoricProcessInstanceQueryProperty.TENANT_ID);
	}

	@ApiOperation("流程实例查询")
	@GetMapping(value = "/process-instances", name = "流程实例查询")
	public PageResponse getProcessInstances(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
			query.processInstanceId(requestParams.get("processInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionName"))) {
			query.processDefinitionName(requestParams.get("processDefinitionName"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionKey"))) {
			query.processDefinitionKey(requestParams.get("processDefinitionKey"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionId"))) {
			query.processDefinitionId(requestParams.get("processDefinitionId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("businessKey"))) {
			query.processInstanceBusinessKey(requestParams.get("businessKey"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("involvedUser"))) {
			query.involvedUser(requestParams.get("involvedUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finished"))) {
			boolean isFinished = ObjectUtils.convertToBoolean(requestParams.get("finished"));
			if (isFinished) {
				query.finished();
			} else {
				query.unfinished();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("superProcessInstanceId"))) {
			query.superProcessInstanceId(requestParams.get("superProcessInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("excludeSubprocesses"))) {
			query.excludeSubprocesses(ObjectUtils.convertToBoolean(requestParams.get("excludeSubprocesses")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finishedAfter"))) {
			query.finishedAfter(ObjectUtils.convertToDatetime(requestParams.get("finishedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finishedBefore"))) {
			query.finishedBefore(ObjectUtils.convertToDatetime(requestParams.get("finishedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedAfter"))) {
			query.startedAfter(ObjectUtils.convertToDatetime(requestParams.get("startedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedBefore"))) {
			query.startedBefore(ObjectUtils.convertToDatetime(requestParams.get("startedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedBy"))) {
			query.startedBy(requestParams.get("startedBy"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			query.processInstanceTenantIdLike(requestParams.get("tenantId"));
		}
		//只显示未完成和未删除的实例
//		query.notDeleted();
//		query.unfinished();
		return new ProcessInstancePaginateList(restResponseFactory).paginateList(getPageable(requestParams), query, allowedSortProperties);
	}

	@ApiOperation("根据流程实例ID查询流程实例")
	@GetMapping(value = "/process-instances/{processInstanceId}", name = "根据ID获取流程实例")
	public ProcessInstanceDetailResponse getProcessDefinition(@PathVariable String processInstanceId) {
		ProcessInstance processInstance = null;
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (historicProcessInstance.getEndTime() == null) {
			processInstance = getProcessInstanceFromRequest(processInstanceId);
		}
		return restResponseFactory.createProcessInstanceDetailResponse(historicProcessInstance, processInstance);
	}

	@ApiOperation("创建流程实例")
	@PostMapping(value = "/process-instances", name = "流程实例创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ProcessInstanceStartResponse startProcessInstance(@RequestBody ProcessInstanceStartRequest request) {

		Map<String,Object> completeVariables = new HashMap<String,Object>();
		//ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(request.getProcessDefinitionId()).singleResult();
		//获取所有任务节点信息
		BpmnModel bpmnModel = repositoryService.getBpmnModel(request.getProcessDefinitionId());
		Process process = bpmnModel.getProcesses().get(0);
		Collection<UserTask> flowElements = process.findFlowElementsOfType(UserTask.class);
		for (UserTask userTask : flowElements) {
			List<String> listCandidateUsers = new ArrayList<String>();
			List<String> listCandidateGroups = userTask.getCandidateGroups();
			listCandidateUsers = userTask.getCandidateUsers();
			if (listCandidateGroups.size() != 0) {
				for (String groups : listCandidateGroups) {
					listCandidateUsers = userGroupRepository.getUserGroup(Integer.valueOf(groups));
				}
			}
			if (listCandidateUsers.size() != 0) {
				completeVariables.put(userTask.getName(), listCandidateUsers);
			}
		}
		if (request.getProcessDefinitionId() == null && request.getProcessDefinitionKey() == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.PARAM_NOT_FOUND, "processDefinitionId或者processDefinitionKey");
		}

		int paramsSet = ((request.getProcessDefinitionId() != null) ? 1 : 0) + ((request.getProcessDefinitionKey() != null) ? 1 : 0);

		if (paramsSet != 1) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_START_PARAM_TO_MANY);
		}

		//判断租户是否为null
		if (request.isCustomTenantSet()) {
			if (request.getProcessDefinitionId() != null) {
				exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_START_TENANT_ERROR);
			}
		}

		Map<String, Object> startVariables = null;
		//判断是否有变量
		if (request.getVariables() != null) {
			startVariables = new HashMap<String, Object>();
			for (RestVariable variable : request.getVariables()) {
				if (variable.getName() == null) {
					exceptionFactory.throwIllegalArgument(ErrorConstant.PARAM_NOT_FOUND, "Variable name");
				}
				startVariables.put(variable.getName(), restResponseFactory.getVariableValue(variable));
			}
		}
		TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
		org.flowable.engine.common.impl.identity.Authentication.setAuthenticatedUserId(tokenUserIdUtils.tokenUserId());
		
		ProcessInstance instance = null;
		identityService.setAuthenticatedUserId(tokenUserIdUtils.tokenUserId());
		if (request.getProcessDefinitionId() != null) {
			//启动流程
			ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processDefinitionId(request.getProcessDefinitionId()).singleResult();
			if(processInstance == null){
				instance = runtimeService.startProcessInstanceById(request.getProcessDefinitionId(), request.getBusinessKey(), completeVariables);
				loggerConverter.save("启动了流程 '" + instance.getProcessDefinitionName() + "'");
			}
		} else if (request.getProcessDefinitionKey() != null) {
			if (request.isCustomTenantSet()) {
				instance = runtimeService.startProcessInstanceByKeyAndTenantId(request.getProcessDefinitionKey(), request.getBusinessKey(), startVariables, request.getTenantId());
			} else {
				instance = runtimeService.startProcessInstanceByKey(request.getProcessDefinitionKey(), request.getBusinessKey(), startVariables);
			}
		}

		//是否自动提交任务
		if (request.isAutoCommitTask()) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
			for (Task task : tasks) {
				if (ObjectUtils.isEmpty(task.getAssignee())) {
					taskService.setAssignee(task.getId(), tokenUserIdUtils.tokenUserId());
				}
				taskService.complete(task.getId());
			}
		}
		if(instance == null){
			exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_NOT_REPEAT,request.getProcessDefinitionId());
		}
		//创建任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
		//处理 可阅人
		taskReadService.taskRead(flowElements,tasks);
		taskCompleteResource.PhpServiceForTask(tasks);
		ProcessInstanceStartResponse response = restResponseFactory.createProcessInstanceStartResponse(instance, tasks);
        taskAsync.TaskAsync(tasks);
		return	response;
	}

	@ApiOperation("删除流程实例")
	@DeleteMapping(value = "/process-instances/{processInstanceId}", name = "流程实例删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteProcessInstance(@PathVariable String processInstanceId, @RequestParam(value = "deleteReason", required = false) String deleteReason, @RequestParam(value = "cascade", required = false) boolean cascade) {
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (historicProcessInstance.getEndTime() != null) {

			List<HistoricTaskDoMain> historicTaskDoMains = taskRepository.queryHistoricTaskInstanceByProcessInstanceId(historicProcessInstance.getId());
			for(HistoricTaskDoMain main: historicTaskDoMains){
				List<PhpUserTaskRequest> phpUserTaskRequestList = phpUserTaskRepository.findByTaskId(main.getId());
				for(PhpUserTaskRequest phptask : phpUserTaskRequestList){
					phpUserTaskRepository.deleteByTaskId(phptask.getTaskId());
					phpService.deletePhpTask(phptask.getPhpTaskId());
				}
			}
			historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
			return;
		}
		ExecutionEntity executionEntity = (ExecutionEntity) getProcessInstanceFromRequest(processInstanceId);
		if (ObjectUtils.isNotEmpty(executionEntity.getSuperExecutionId())) {
			exceptionFactory.throwForbidden(ErrorConstant.INSTANCE_HAVE_PARENT, processInstanceId);
		}

		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		if (cascade) {
			List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
			for(HistoricTaskInstance historicTaskInstance : list){
				String taskId = historicTaskInstance.getId();
				List<PhpUserTaskRequest> phpUserTaskRequestList = phpUserTaskRepository.findByTaskId(taskId);
				for(PhpUserTaskRequest phptask : phpUserTaskRequestList){
					phpUserTaskRepository.deleteByTaskId(phptask.getTaskId());
					phpService.deletePhpTask(phptask.getPhpTaskId());
				}
			}
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}else{

		}

		loggerConverter.save("删除了流程实例 '" +historicProcessInstance.getProcessDefinitionName()+ "'");
	}

}
