package com.liansen.form.domain;

import com.liansen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_fo_model database table.
 * 
 */
@Entity
@Table(name = "pw_fo_layout", catalog = "liansen_form")
@NamedQuery(name = "FormLayout.findAll", query = "SELECT f FROM FormLayout f")
public class FormLayout extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("表单编辑器内容ID")
	private int editorSourceId;

	@ApiModelProperty("布局名称")
	private String name;

	@ApiModelProperty("数据表ID")
	private int tableId;


	public FormLayout() {
	}

	@Column(name = "editor_source_id_")
	public int getEditorSourceId() {
		return this.editorSourceId;
	}

	public void setEditorSourceId(int editorSourceId) {
		this.editorSourceId = editorSourceId;
	}

	@Column(name = "name_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "table_id_")
	public int getTableId() {
		return this.tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}


}