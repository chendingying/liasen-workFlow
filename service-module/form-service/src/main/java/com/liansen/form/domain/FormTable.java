package com.liansen.form.domain;

import com.liansen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_fo_table database table.
 * 
 */
@Entity
@Table(name = "pw_fo_table", catalog = "liansen_form")
@NamedQuery(name = "FormTable.findAll", query = "SELECT f FROM FormTable f")
public class FormTable extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("数据表名称")
	private String name;

	@ApiModelProperty("数据表标识")
	private String key;

	@ApiModelProperty("数据表分类")
	private String category;

	@ApiModelProperty("数据表备注")
	private String remark;

	public FormTable() {
	}

	@Column(name = "name_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "key_")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "category_")
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "remark_")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}