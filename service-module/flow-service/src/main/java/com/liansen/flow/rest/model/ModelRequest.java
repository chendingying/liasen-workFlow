package com.liansen.flow.rest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author cdy
 * @create 2018/9/4
 */
public class ModelRequest {

    @ApiModelProperty("流程名称")
    protected String name;

    @ApiModelProperty("流程标识")
    protected String key;

    @ApiModelProperty("类别")
    protected String category;

    protected String description;

    @ApiModelProperty("租户Id")
    protected String tenantId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getMetaInfo() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode metaInfo = objectMapper.createObjectNode();
        metaInfo.put("name", name);
        metaInfo.put("description", description);
        metaInfo.put("process_namespace",category);
        return metaInfo.toString();
    }
}
