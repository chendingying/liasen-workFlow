package com.liansen.identity.domain;

import com.liansen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_id_group database table.
 *
 */
@Entity
@Table(name = "workflow_group", catalog = "ksh")
@NamedQuery(name = "Group.findAll", query = "SELECT d FROM Group d")
public class Group extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("群组名称")
	private String name;

	@ApiModelProperty("群组类型 0 父级部门 1 子级部门")
	private byte type;

	@ApiModelProperty("序号")
	private int order;

	@ApiModelProperty("父级群组Id")
	private int parentId;

	@ApiModelProperty("状态")
	private byte status;

	@ApiModelProperty("备注")
	private String remark;

	public Group() {
	}

	@Column(name = "name_", nullable = false, length = 255)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type_", nullable = false)
	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Column(name = "order_", nullable = false)
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "parent_id_", nullable = false)
	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Column(name = "status_", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Column(name = "remark_", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}