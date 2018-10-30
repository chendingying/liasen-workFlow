package com.liansen.flow.rest.phpClient.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.flow.rest.phpClient.request.PhpUserTaskRequest;

import java.util.List;

/**
 * Created by CDZ on 2018/10/29.
 */
public interface PhpUserTaskRepository  extends BaseRepository<PhpUserTaskRequest, Integer> {
    List<PhpUserTaskRequest> findByTaskId(String taskId);
    void deleteByTaskId(String taskId);
    PhpUserTaskRequest findByUserIdAndTaskId(String userId,String taskId);
}
