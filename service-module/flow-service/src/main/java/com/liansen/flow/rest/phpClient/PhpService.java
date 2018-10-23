package com.liansen.flow.rest.phpClient;

import com.liansen.flow.rest.phpClient.repository.PhpTaskRepository;
import com.liansen.flow.rest.phpClient.request.PhpTaskRequest;
import com.liansen.flow.rest.task.TaskResponse;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;

/**
 * Created by CDZ on 2018/10/20.
 */
@Service
public class PhpService {
    @Autowired
    PhpTaskRepository phpTaskRepository;

    public String phpTaskService(TaskResponse task){
        DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
        PhpTaskRequest phpTaskRequest = new PhpTaskRequest();
        phpTaskRequest.setUsername(task.getAssignee());
//        phpTaskRequest.setUrl("http://192.168.249.211:9001/#/flow/task/?id="+task.getId());
        phpTaskRequest.setUrl("http://192.168.249.211:66/work-admin/#/flow/task/?id="+task.getId());
        phpTaskRequest.setTitle(task.getName());
        phpTaskRequest.setStarttime(df1.format(task.getCreateTime()));
        if(task.getDueDate() != null){
            phpTaskRequest.setEndtime(df1.format(task.getDueDate()));
        }else{
            phpTaskRequest.setEndtime("");
        }
        return phpTaskRepository.getEngineMesasge(phpTaskRequest);
    }
    public String deletePhpTask(String phpTaskId){
        return phpTaskRepository.deletePhpTask(phpTaskId);
    }

}
