package com.liansen.flow.rest.readTask;

import com.liansen.flow.rest.readTask.domain.TaskReadRequest;
import com.liansen.flow.rest.readTask.repository.TaskReadRepository;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 可阅人
 * Created by CDZ on 2018/10/31.
 */
@Service
public class TaskReadService {
    @Autowired
    TaskReadRepository taskReadRepository;

    public void taskRead(Collection<UserTask> userTasks, List<Task> taskList){

        if(userTasks == null){
            return;
        }
        for(UserTask userTask : userTasks){
            if(userTask.getExtensionElements() == null){
                return;
            }
            for(Task task : taskList){
                if(userTask.getExtensionElements().get("readusertaskassignment") == null){
                    return;
                }
                String user = userTask.getExtensionElements().get("readusertaskassignment").get(0).getElementText();
                if(user.equals("")){
                    return;
                }
                String[] userId = user.split(",");
                for(String id : userId){
                    TaskReadRequest taskReadRequest = new TaskReadRequest();
                    taskReadRequest.setUserId(id);
                    taskReadRequest.setProcessDefinitionId(task.getProcessDefinitionId());
                    taskReadRequest.setStatus("1");
                    taskReadRequest.setTaskId(task.getId());
                    taskReadRepository.save(taskReadRequest);
                }
            }
        }
    }
}
