package com.liansen.flow.rest.log;

import com.liansen.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by CDZ on 2018/9/21.
 */
public interface LoggerRepository extends BaseRepository<Logger, Integer> {

    @Query("select l from Logger l where l.userId = ?1 and TO_DAYS(l.lastUpdateTime)= to_days(now()) ORDER BY l.lastUpdateTime desc")
    public List<Logger> selectLogger(String userId);

}
