package com.liansen.flow.rest.task.resource;

import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.constant.TableConstant;
import com.liansen.flow.rest.common.IdentityRequest;
import com.liansen.flow.rest.common.IdentityResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务候选信息接口
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "任务候选信息接口")
@RestController
public class TaskIdentityResource extends BaseTaskResource {
    @Autowired
    RuntimeService runtimeService;
    @ApiOperation("任务候选信息查询")
    @GetMapping(value = "/tasks/{taskId}/identity-links", name = "任务候选信息查询")
    public List<IdentityResponse> getIdentityLinks(@PathVariable String taskId) {
        HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
        List<HistoricIdentityLink> historicIdentityLinks = historyService.getHistoricIdentityLinksForTask(task.getId());
        return restResponseFactory.createTaskIdentityResponseList(historicIdentityLinks);
    }

    @Test
    public void startProcessInstanceByKey() {
        Map<String, Object> vars = new HashMap<>();
        String[] v = {"9"};
        vars.put("通过与不通过测试", Arrays.asList(v));
        String processDefinitionKey = "ky:2:225004";
        ProcessInstance startProcessInstanceByKey = runtimeService.startProcessInstanceByKey(processDefinitionKey,
                vars);
    }

    @ApiOperation("创建任务候选信息")
    @PostMapping(value = "/tasks/{taskId}/identity-links", name = "任务候选信息创建")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public void createIdentityLink(@PathVariable String taskId, @RequestBody IdentityRequest taskIdentityRequest) {
        Task task = getTaskFromRequest(taskId);
        validateIdentityLinkArguments(taskIdentityRequest.getIdentityId(), taskIdentityRequest.getType());
        if (TableConstant.IDENTITY_GROUP.equals(taskIdentityRequest.getType())) {
            taskService.addGroupIdentityLink(task.getId(), taskIdentityRequest.getIdentityId(), IdentityLinkType.CANDIDATE);
        } else if (TableConstant.IDENTITY_USER.equals(taskIdentityRequest.getType())) {
            taskService.addUserIdentityLink(task.getId(), taskIdentityRequest.getIdentityId(), IdentityLinkType.CANDIDATE);

        }
        loggerConverter.save("创建了任务候选信息 '" + task.getName() + "'");
    }

    @ApiOperation("删除任务候选信息")
    @DeleteMapping(value = "/tasks/{taskId}/identity-links/{type}/{identityId}",name = "任务候选信息删除")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteIdentityLink(@PathVariable("taskId") String taskId,@PathVariable("identityId") String identityId,@PathVariable("type") String type) {
        Task task = getTaskFromRequest(taskId);

        validateIdentityLinkArguments(identityId, type);

        validateIdentityExists(taskId, identityId, type);

        if (TableConstant.IDENTITY_GROUP.equals(type)) {
            taskService.deleteGroupIdentityLink(task.getId(), identityId, IdentityLinkType.CANDIDATE);
        } else if (TableConstant.IDENTITY_USER.equals(type)) {
            taskService.deleteUserIdentityLink(task.getId(), identityId, IdentityLinkType.CANDIDATE);
        }
        loggerConverter.save("删除了任务候选信息 '" + task.getName() + "'");
    }
    private void validateIdentityLinkArguments(String identityId, String type) {
        if (identityId == null) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.TASK_IDENTITY_ID_NOT_FOUND);
        }
        if (type == null) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.TASK_IDENTITY_TYPE_NOT_FOUND);
        }

        if (!TableConstant.IDENTITY_GROUP.equals(type) && !TableConstant.IDENTITY_USER.equals(type)) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.TASK_IDENTITY_TYPE_ERROR);
        }
    }

    private void validateIdentityExists(String taskId, String identityId, String type) {
        List<IdentityLink> allLinks = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink link : allLinks) {
            boolean rightIdentity = false;
            if (TableConstant.IDENTITY_USER.equals(type)) {
                rightIdentity = identityId.equals(link.getUserId());
            } else {
                rightIdentity = identityId.equals(link.getGroupId());
            }

            if (rightIdentity && link.getType().equals(IdentityLinkType.CANDIDATE)) {
                return;
            }
        }
        exceptionFactory.throwObjectNotFound(ErrorConstant.TASK_IDENTITY_NOT_FOUND);
    }
}





















