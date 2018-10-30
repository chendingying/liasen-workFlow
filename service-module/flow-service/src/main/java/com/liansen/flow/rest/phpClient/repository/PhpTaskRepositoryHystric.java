package com.liansen.flow.rest.phpClient.repository;

import com.liansen.flow.rest.phpClient.request.PhpTaskRequest;
import com.liansen.flow.rest.phpClient.request.PhpTaskStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by CDZ on 2018/10/26.
 */
@Component
public class PhpTaskRepositoryHystric implements PhpTaskRepository {

    @Override
    public String getEngineMesasge(@RequestBody PhpTaskRequest phpTaskRequest) {
        return "连接超时";
    }

    @Override
    public String deletePhpTask(@PathVariable(value = "phpTaskId") String phpTaskId) {
        return "连接超时";
    }

    @Override
    public String modify(@RequestBody PhpTaskStatus phpTaskStatus){return "连接超时";}
}
