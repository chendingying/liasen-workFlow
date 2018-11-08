package com.liansen.flow.rest.task.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.flow.rest.task.domain.HistoricTaskDoMain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by CDZ on 2018/10/31.
 */
public interface TaskRepository extends BaseRepository<HistoricTaskDoMain, Integer>{
    @Query(value = "select * from ACT_HI_TASKINST hi where hi.PROC_DEF_ID_ in(select r.process_definition_id from act_read_task r where r.user_id = '123')" +
            " UNION" +
            " select * from ACT_HI_TASKINST where 1=1 limit ?1",nativeQuery = true)
    List<HistoricTaskDoMain> queryHistoricTaskInstance();

    @Query(value = "select * from ACT_HI_TASKINST where PROC_INST_ID_ = ?1",nativeQuery = true)
    List<HistoricTaskDoMain> queryHistoricTaskInstanceByProcessInstanceId(String processInstanceId);
}
