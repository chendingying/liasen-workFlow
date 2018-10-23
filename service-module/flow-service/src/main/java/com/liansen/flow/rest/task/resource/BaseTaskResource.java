package com.liansen.flow.rest.task.resource;

import com.liansen.common.resource.BaseResource;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.rest.RestResponseFactory;
import com.liansen.flow.rest.log.LoggerConverter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author cdy
 * @create 2018/9/5
 */
public class BaseTaskResource extends BaseResource {

    @Autowired
    protected RestResponseFactory restResponseFactory;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected HistoryService historyService;

    @Autowired
    protected LoggerConverter loggerConverter;

    protected Task getTaskFromRequest(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.TASK_NOT_FOUND,taskId);
        }
        return task;
    }

    protected HistoricTaskInstance getHistoricTaskFromRequest(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if(task == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.TASK_NOT_FOUND,taskId);
        }
        return task;
    }
























}
