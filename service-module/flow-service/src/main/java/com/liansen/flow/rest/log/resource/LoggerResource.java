package com.liansen.flow.rest.log.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.flow.rest.log.Logger;
import com.liansen.flow.rest.log.LoggerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 日志
 * Created by CDZ on 2018/9/21.
 */
@Api(description = "日志接口")
@RestController
public class LoggerResource extends BaseResource {

    @Autowired
    LoggerRepository loggerRepository;

    /**
     * 查询当前用户下当天日志
     * @param userId
     * @return
     */
    @ApiOperation("查询当前用户当天日志信息")
    @GetMapping(value = "/loggers/userId/{userId}")
    public List<Logger> getUserLoggers(@PathVariable String userId){
        return loggerRepository.selectLogger(userId);

    }

    /**
     * 查询所有用户的日志
     * @param requestParams
     * @return
     */
    @ApiOperation("查询所有用户日志信息")
    @GetMapping(value = "/loggers/userId")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormDefinition(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<Logger> criteria = new Criteria<Logger>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("desc", requestParams.get("desc")));
        criteria.add(Restrictions.like("userId", requestParams.get("userId")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(loggerRepository.findAll(criteria, getPageable(requestParams)));
    }
}
