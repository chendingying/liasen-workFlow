package com.liansen.flow.rest.task.resource;

import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.rest.phpClient.PhpService;
import com.liansen.flow.rest.phpClient.repository.PhpTaskRepository;
import com.liansen.flow.rest.phpClient.repository.PhpUserTaskRepository;
import com.liansen.flow.rest.phpClient.request.PhpUserTaskRequest;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
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

    //表示每隔1分钟
    @Scheduled(fixedRate=60000)
    public void fixedRateJob() throws ParseException {
        List<Task> taskList = taskService.createTaskQuery().list();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date systemDate =f.parse(f.format(new Date())); //这是获取当前时间
        for(Task task : taskList){
            if(task.getDueDate() == null){
                continue;
            }
            if(systemDate.getTime() > task.getDueDate().getTime()){
                List<PhpUserTaskRequest> phpUserTaskRequest = phpUserTaskRepository.findByTaskId(task.getId());
                if(phpUserTaskRequest == null){
                    return;
                }
                for(PhpUserTaskRequest userTask : phpUserTaskRequest){
                    if(userTask.getTrigger()){
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
