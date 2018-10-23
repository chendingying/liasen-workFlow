package com.liansen.flow.rest.phpClient.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.flow.rest.phpClient.request.PhpTaskIdAndTaskId;

import java.util.List;

/**
 * Created by CDZ on 2018/10/22.
 */
public interface PhpTaskAndTaskRepository extends BaseRepository<PhpTaskIdAndTaskId, Integer> {
    List<PhpTaskIdAndTaskId> findByTaskId(String taskId);
    void deleteByTaskId(String taskId);
}
