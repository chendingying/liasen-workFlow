package com.liansen.flow.rest.phpClient.repository;

import org.flowable.idm.api.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by CDZ on 2018/10/23.
 */
@FeignClient(url = "http://localhost:8082",name="engine")
public interface UserGroupRepository {
    @RequestMapping(value = "/groups/{id}/usersId",method = RequestMethod.GET)
    public List<String> getUserGroup(@PathVariable(value = "id") Integer id);

    @RequestMapping(value = "/groups/userId/{userId}" ,method = RequestMethod.GET)
    public List<String> getGroupByUserId(@PathVariable(value = "userId") Integer userId1);
}
