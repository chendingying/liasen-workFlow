package com.liansen.form.domain;



import com.liansen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the pw_fo_definition database table.
 * 
 */
@Entity
@Table(name = "pw_fo_definition", catalog = "liansen_form")
@NamedQuery(name = "FormDefinition.findAll", query = "SELECT f FROM FormDefinition f")
public class FormDefinition extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("表单分类")
	private String category;

	@ApiModelProperty("表单部署内容ID")
	private Integer deploySourceId;

	@ApiModelProperty("表单标识")
	private String key;

	@ApiModelProperty("表单名称")
	private String name;

	@ApiModelProperty("状态值0:正常 1:挂起")
	private byte suspensionState;

	@ApiModelProperty("数据表ID")
	private int tableId;

	@ApiModelProperty("版本")
	private int version;

	@ApiModelProperty("注释")
	private String remark;


	public FormDefinition() {
	}

	@Column(name = "category_")
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "deploy_source_id_")
	public Integer getDeploySourceId() {
		return this.deploySourceId;
	}

	public void setDeploySourceId(Integer deploySourceId) {
		this.deploySourceId = deploySourceId;
	}

	@Column(name = "key_")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "name_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "suspension_state_")
	public byte getSuspensionState() {
		return this.suspensionState;
	}

	public void setSuspensionState(byte suspensionState) {
		this.suspensionState = suspensionState;
	}

	@Column(name = "table_id_")
	public int getTableId() {
		return this.tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "remark_")
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
}