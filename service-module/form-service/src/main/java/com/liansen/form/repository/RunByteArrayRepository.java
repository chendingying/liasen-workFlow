package com.liansen.form.repository;


import com.liansen.common.repository.BaseRepository;
import com.liansen.form.domain.RunByteArray;

/**
 * Created by CDZ on 2018/9/15.
 */
public interface RunByteArrayRepository extends BaseRepository<RunByteArray, Integer> {
    RunByteArray findByProcInstId(String procInstId);
}
