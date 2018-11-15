package com.liansen.flow.rest.phpClient;

import com.liansen.common.utils.TokenUserIdUtils;
import com.liansen.flow.rest.phpClient.repository.PhpTaskRepository;
import com.liansen.flow.rest.phpClient.repository.PhpUserTaskRepository;
import com.liansen.flow.rest.phpClient.request.PhpTaskRequest;
import com.liansen.flow.rest.phpClient.request.PhpTaskStatus;
import com.liansen.flow.rest.phpClient.request.PhpUserTaskRequest;
import com.liansen.flow.rest.task.TaskResponse;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by CDZ on 2018/10/20.
 */
@Service
public class PhpService {
    @Autowired
    PhpTaskRepository phpTaskRepository;

    @Autowired
    PhpUserTaskRepository phpUserTaskRepository;

    public String phpTaskService(TaskResponse task){
        TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
        DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
        PhpTaskRequest phpTaskRequest = new PhpTaskRequest();
        phpTaskRequest.setUsername(task.getAssignee());
        phpTaskRequest.setUrl("192.168.249.211:66/work-admin/#/flow/task/?id="+task.getId()+"&userId="+task.getUserId());
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

    public String modify(Boolean finish,String id){
        PhpTaskStatus phpTaskStatus = new PhpTaskStatus();
        phpTaskStatus.setFinish(finish);
        phpTaskStatus.setId(id);
        return phpTaskRepository.modify(phpTaskStatus);
    }

    public String modify(Boolean finish,String id,String taskId){
        List<PhpUserTaskRequest> requests =  phpUserTaskRepository.findByTaskId(taskId);
        for(PhpUserTaskRequest phpUserTaskRequest : requests ){
            PhpTaskStatus phpTaskStatus = new PhpTaskStatus();
            phpTaskStatus.setFinish(finish);
            phpTaskStatus.setId(phpUserTaskRequest.getPhpTaskId());
            phpTaskRepository.modify(phpTaskStatus);
        }
        return "";
    }
}
