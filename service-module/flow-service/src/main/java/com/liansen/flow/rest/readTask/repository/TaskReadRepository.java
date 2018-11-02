package com.liansen.flow.rest.readTask.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.flow.rest.readTask.domain.TaskReadRequest;

import java.util.List;

/**
 * Created by CDZ on 2018/10/31.
 */
public interface TaskReadRepository  extends BaseRepository<TaskReadRequest, Integer> {
    List<TaskReadRequest> findByUserId(String userId);
}
