package com.liansen.flow.rest.phpClient.repository;

import com.liansen.flow.rest.phpClient.request.PhpTaskRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by CDZ on 2018/10/20.
 */
@FeignClient(url = "http://123.56.2.28:8000",name="engine")
public interface PhpTaskRepository {
    @RequestMapping(value="/admin/api/add",method= RequestMethod.POST)
    public String getEngineMesasge(@RequestBody PhpTaskRequest phpTaskRequest);

    @RequestMapping(value = "/admin/api/delete/{phpTaskId}",method = RequestMethod.POST)
    public String deletePhpTask(@PathVariable(value="phpTaskId")  String phpTaskId);
}
