package com.liansen.flow.rest.phpClient.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by CDZ on 2018/10/26.
 */
@Component
public class UserGroupHystric implements UserGroupRepository {
    @Override
    public List<String> getUserGroup(@PathVariable(value = "id") Integer id) {
        return null;
    }

    @Override
    public List<String> getGroupByUserId(@PathVariable(value = "userId") Integer userId1) {
        return null;
    }
}
