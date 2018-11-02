package com.liansen.flow.rest.readTask.resource;

import com.liansen.common.utils.ObjectUtils;
import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.rest.readTask.domain.TaskReadRequest;
import com.liansen.flow.rest.readTask.repository.TaskReadRepository;
import io.swagger.annotations.Api;
import org.flowable.engine.HistoryService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by CDZ on 2018/10/31.
 */
@Api(description = "任务可阅接口")
@RestController
public class TaskReadResource {
    @Autowired
    TaskReadRepository taskReadRepository;

    @Autowired
    HistoryService historyService;
    @GetMapping("/taskRead")
    public List<HistoricTaskInstance> taskRead(){
        TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
        List<HistoricTaskInstance> tasks = null;
        List<TaskReadRequest> taskReadRequests = taskReadRepository.findByUserId("123");
        for(TaskReadRequest taskReadRequest: taskReadRequests){
           tasks =  historyService.createHistoricTaskInstanceQuery().processDefinitionId(taskReadRequest.getProcessDefinitionId()).list();
        }
        return tasks;
    }
}
