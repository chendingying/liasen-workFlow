package com.liansen.flow.rest.task.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.task.api.Task;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务认领接口
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "任务认领接口")
@RestController
public class TaskClaimResource extends BaseTaskResource {

    @ApiOperation("任务认领")
    @PutMapping(value = "/tasks/{taskId}/claim/{claimer}",name = "任务认领")
    @Transactional
    public void claimTask(@PathVariable("taskId") String taskId,@PathVariable("claimer") String claimer) {
        Task task = getTaskFromRequest(taskId);
        taskService.claim(task.getId(),claimer);
        loggerConverter.save("认领任务 '" + task.getName() + "'");
    }
}
