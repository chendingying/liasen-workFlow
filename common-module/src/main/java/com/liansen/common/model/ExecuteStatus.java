package com.liansen.common.model;

import io.swagger.annotations.ApiModelProperty;


/**
 * ExecuteStatus
 *
 * @author CDY
 * @time 2017/7/7.
 */
public class ExecuteStatus  {

    @ApiModelProperty("响应代码")
    private Integer result;
    @ApiModelProperty("响应状态")
    private String message;

    private String id;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
