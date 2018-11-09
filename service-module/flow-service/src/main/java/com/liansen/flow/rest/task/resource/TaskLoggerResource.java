package com.liansen.flow.rest.task.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.PageResponse;
import com.liansen.flow.rest.task.domain.HistoricLogger;
import com.liansen.flow.rest.task.repository.HistoricLoggerRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * Created by CDZ on 2018/11/9.
 */
@RestController
public class TaskLoggerResource extends BaseTaskResource {

    @Autowired
    HistoricLoggerRepository historicLoggerRepository;


    @ApiOperation("查询所有用户日志信息")
    @GetMapping(value = "/task/logger")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getTaskLogger(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<HistoricLogger> criteria = new Criteria<HistoricLogger>();
        criteria.add(Restrictions.eq("procDefId", requestParams.get("procDefId")));
        return createPageResponse(historicLoggerRepository.findAll(criteria, getPageable(requestParams)));
    }
}
