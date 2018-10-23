package com.liansen.form.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * Created by CDZ on 2018/10/18.
 */
public class FormDefinitionRequest {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("表单分类")
    private String category;


    @ApiModelProperty("表单标识")
    private String key;

    @ApiModelProperty("表单名称")
    private String name;


    @ApiModelProperty("模型版本号")
    private int version;

    @ApiModelProperty("数据表ID")
    private int tableId;

    private int id;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
