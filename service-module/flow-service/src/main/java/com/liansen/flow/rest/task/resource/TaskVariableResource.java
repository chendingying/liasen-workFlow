package com.liansen.flow.rest.task.resource;


import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.rest.variable.RestVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程变量接口
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "任务变量接口")
@RestController
public class TaskVariableResource extends BaseTaskResource{

    @ApiOperation("任务变量查询")
    @GetMapping(value = "/tasks/{taskId}/variables", name = "获取任务变量")
    public List<RestVariable> getExecutionVariables(@PathVariable String taskId) {
        HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
        if (task.getEndTime() == null) {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            return restResponseFactory.createRestVariables(variables);
        } else {
            List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
            return restResponseFactory.createRestVariables(historicVariableInstances);
        }
    }

    @ApiOperation("创建任务变量")
    @PostMapping(value = "/tasks/{taskId}/variables", name = "创建流程实例变量")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public void createExecutionVariable(@PathVariable String taskId, @RequestBody RestVariable restVariable) {
        Task task = getTaskFromRequest(taskId);
        if (restVariable.getName() == null) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_VAR_NAME_NOT_FOUND);
        }
        taskService.setVariable(task.getId(), restVariable.getName(), restResponseFactory.getVariableValue(restVariable));
        loggerConverter.save("创建了任务变量 '" + task.getName() + "'");
    }

    @ApiOperation("删除任务变量")
    @DeleteMapping(value = "/tasks/{taskId}/variables/{variableName}", name = "删除任务变量")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteExecutionVariable(@PathVariable String taskId, @PathVariable("variableName") String variableName) {
        Task task = getTaskFromRequest(taskId);
        taskService.removeVariable(task.getId(), variableName);
        loggerConverter.save("删除了任务变量 '" + task.getName() + "'");
    }
}
