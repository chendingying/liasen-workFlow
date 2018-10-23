package com.liansen.flow.rest.definition.resource;

import com.liansen.common.constant.CoreConstant;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.constant.TableConstant;
import com.liansen.flow.rest.definition.ProcessDefinitionResponse;
import com.liansen.flow.rest.definition.ProcessDefinitionsPaginateList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ProcessDefinitionQueryProperty;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.job.api.Job;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程定义基础接口类
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "流程定义基础接口")
@RestController
public class ProcessDefinitionResource extends BaseProcessDefinitionResource {

	private static final Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("id", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("key", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
		allowedSortProperties.put("category", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
		allowedSortProperties.put("name", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
		allowedSortProperties.put("version", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
		allowedSortProperties.put("tenantId", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
	}

	@ApiOperation("流程定义查询")
	@GetMapping(value = "/process-definitions", name = "流程定义查询")
	public PageResponse getProcessDefinitions(@ApiIgnore @RequestParam Map<String, String> requestParams) {

		//获取token下的用户信息
		TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
		//token失效
		if(tokenUserIdUtils == null){
			exceptionFactory.throwAuthError(CoreConstant.HEADER_TOKEN_NOT_FOUND);
		}
		if(!tokenUserIdUtils.tokenUserId().equals(TableConstant.ADMIN_USER_ID)){
				requestParams.put("startableByUser",tokenUserIdUtils.tokenUserId());
		}

		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		if (ObjectUtils.isNotEmpty(requestParams.get("id"))) {
			processDefinitionQuery.processDefinitionId(requestParams.get("id"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("category"))) {
			processDefinitionQuery.processDefinitionCategoryLike(ObjectUtils.convertToLike(requestParams.get("category")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("key"))) {
			processDefinitionQuery.processDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("key")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("name"))) {
			processDefinitionQuery.processDefinitionNameLike(ObjectUtils.convertToLike(requestParams.get("name")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("version"))) {
			processDefinitionQuery.processDefinitionVersion(ObjectUtils.convertToInteger(requestParams.get("version")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("suspended"))) {
			boolean suspended = ObjectUtils.convertToBoolean(requestParams.get("suspended"));
			if (suspended) {
				processDefinitionQuery.suspended();
			} else {
				processDefinitionQuery.active();
			}
		}
		//判断是否是最新版本
		if (ObjectUtils.isNotEmpty(requestParams.get("latestVersion"))) {
			boolean latest = ObjectUtils.convertToBoolean(requestParams.get("latestVersion"));
			if (latest) {
				processDefinitionQuery.latestVersion();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startableByUser"))) {
			processDefinitionQuery.startableByUser(requestParams.get("startableByUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			processDefinitionQuery.processDefinitionTenantId(requestParams.get("tenantId"));
		}

		return new ProcessDefinitionsPaginateList(restResponseFactory).paginateList(getPageable(requestParams), processDefinitionQuery, allowedSortProperties);
	}

	@ApiOperation("根据流程定义ID查询流程定义信息")
	@GetMapping(value = "/process-definitions/{processDefinitionId}", name = "根据ID获取流程定义")
	public ProcessDefinitionResponse getProcessDefinition(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		return restResponseFactory.createProcessDefinitionResponse(processDefinition);
	}

	@ApiOperation("删除流程定义信息")
	@DeleteMapping(value = "/process-definitions/{processDefinitionId}", name = "流程定义删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteProcessDefinition(@PathVariable String processDefinitionId, @RequestParam(value = "cascade", required = false, defaultValue = "false") Boolean cascade) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		if (processDefinition.getDeploymentId() == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_DEPLOY_NOT_FOUND, processDefinitionId);
		}

		if (cascade) {
			List<Job> jobs = managementService.createTimerJobQuery().processDefinitionId(processDefinitionId).list();
			for (Job job : jobs) {
				managementService.deleteTimerJob(job.getId());
			}
			repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
		} else {
			long processCount = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).count();
			if (processCount > 0) {
				exceptionFactory.throwForbidden(ErrorConstant.DEFINITION_HAVE_INSTANCE, processDefinitionId);
			}

			long jobCount = managementService.createTimerJobQuery().processDefinitionId(processDefinitionId).count();
			if (jobCount > 0) {
				exceptionFactory.throwForbidden(ErrorConstant.DEFINITION_HAVE_TIME_JOB, processDefinitionId);
			}
			repositoryService.deleteDeployment(processDefinition.getDeploymentId());
		}
		loggerConverter.save("删除了流程定义 '"+processDefinition.getName()+"'");
	}
}
