package com.liansen.flow.rest.model;

import io.swagger.annotations.ApiModelProperty;

public class ModelEditorJsonRequest {

	@ApiModelProperty("流程名称")
	private String name;

	@ApiModelProperty("流程标识")
	private String key;

	private String description;

	@ApiModelProperty("json串")
	private String jsonXml;

	@ApiModelProperty("新版本")
	private boolean newVersion;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJsonXml() {
		return jsonXml;
	}

	public void setJsonXml(String jsonXml) {
		this.jsonXml = jsonXml;
	}

	public boolean isNewVersion() {
		return newVersion;
	}

	public void setNewVersion(boolean newVersion) {
		this.newVersion = newVersion;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
