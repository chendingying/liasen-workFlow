package com.liansen.flow.rest.task.resource;

import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.rest.phpClient.PhpService;
import com.liansen.flow.rest.phpClient.repository.PhpTaskRepository;
import com.liansen.flow.rest.phpClient.repository.PhpUserTaskRepository;
import com.liansen.flow.rest.phpClient.request.PhpUserTaskRequest;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by CDZ on 2018/10/30.
 */
@Component
public class TaskAsync {

    @Autowired
    TaskService taskService;

    @Autowired
    PhpService phpService;

    @Autowired
    PhpUserTaskRepository phpUserTaskRepository;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    //表示每隔1分钟
    @Scheduled(fixedRate=10000)
    public void fixedRateJob() throws ParseException {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().list();
        for(HistoricTaskInstance task : historicTaskInstances){
            if(task.getDueDate() == null){
                continue;
            }
            if(task.getCreateTime().getTime() > task.getDueDate().getTime()){
                List<PhpUserTaskRequest> phpUserTaskRequest = phpUserTaskRepository.findByTaskId(task.getId());
                if(phpUserTaskRequest == null){
                    return;
                }
                for(PhpUserTaskRequest userTask : phpUserTaskRequest){
                    if(userTask.getTrigger() != null){
                        continue;
                    }
                    phpService.modify(false,userTask.getPhpTaskId());
                    userTask.setTrigger(true);
                    phpUserTaskRepository.save(userTask);

                }
            }
        }
    }
}
