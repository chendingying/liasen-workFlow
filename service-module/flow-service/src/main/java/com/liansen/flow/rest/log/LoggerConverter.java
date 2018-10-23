package com.liansen.flow.rest.log;

import com.liansen.common.utils.TokenUserIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CDZ on 2018/9/21.
 */
@Service
public class LoggerConverter {
    @Autowired
    LoggerRepository loggerRepository;


    public void save(String desc){
        TokenUserIdUtils tokenUserIdUtils = new TokenUserIdUtils();
        Logger logger = new Logger();
        logger.setUserId(tokenUserIdUtils.tokenUserId());
        logger.setDesc(desc);
        loggerRepository.save(logger);
    }
}
