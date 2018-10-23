package com.liansen.flow.rest.phpClient.controller;

import com.liansen.flow.rest.phpClient.PhpService;
import com.liansen.flow.rest.phpClient.repository.PhpTaskRepository;
import com.liansen.flow.rest.phpClient.request.PhpTaskRequest;
import com.liansen.flow.rest.task.TaskResponse;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;

/**
 * Created by CDZ on 2018/10/20.
 */
@RestController
public class PhpTaskController {

    @Autowired
    PhpService phpService;
    @RequestMapping(value="/admin/api/add",method= RequestMethod.POST)
    public String getEngineMesasge(@RequestBody TaskResponse taskResponse) {
       return phpService.phpTaskService(taskResponse);
    }


}
