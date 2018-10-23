package com.liansen.common.utils;

import com.liansen.common.domain.PageRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CDZ on 2018/10/16.
 */
public class PageUtils {
    public Map<String, String> requestParams(PageRequest request){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("pageNum",request.getPageNum());
        requestParams.put("pageSize",request.getPageSize());
        requestParams.put("sortName",request.getSortName());
        requestParams.put("sortOrder",request.getSortOrder());
        return requestParams;
    }
}
