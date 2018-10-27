package com.liansen.flow.rest.phpClient.repository;

import com.liansen.flow.rest.phpClient.request.PhpTaskIdAndTaskId;
import com.liansen.flow.rest.phpClient.request.PhpTaskRequest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        return null;
    }

    @Override
    public String deletePhpTask(@PathVariable(value = "phpTaskId") String phpTaskId) {
        return null;
    }
}
