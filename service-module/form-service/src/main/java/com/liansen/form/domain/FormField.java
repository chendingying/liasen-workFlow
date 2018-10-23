package com.liansen.form.domain;

import com.liansen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.UUID;


/**
 * The persistent class for the pw_fo_field database table.
 * 
 */
@Entity
@Table(name = "pw_fo_field", catalog = "liansen_form")
@NamedQuery(name = "FormField.findAll", query = "SELECT f FROM FormField f")
public class FormField extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("字段标识")
	private String key;

	@ApiModelProperty("字段名称")
	private String name;

	@ApiModelProperty("字段备注")
	private String remark;

	@ApiModelProperty("字段状态")
	private byte status;

	@ApiModelProperty("数据表ID")
	private int tableId;

	@ApiModelProperty("字段类型")
	private String type;

	public FormField() {
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		if(key == null || key.isEmpty()) {
			key = UUID.randomUUID().toString();
		}
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

	@Column(name = "remark_")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status_")
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Column(name = "table_id_")
	public int getTableId() {
		return this.tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	@Column(name = "type_")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}