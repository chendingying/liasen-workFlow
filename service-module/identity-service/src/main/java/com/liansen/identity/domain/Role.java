package com.liansen.identity.domain;


import com.liansen.common.domain.BaseEntity;
import com.liansen.common.model.ObjectMap;

import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the pw_id_role database table.
 *
 */
@Entity
@Table(name = "workflow_role", catalog = "ksh")
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
public class Role extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private byte status;
	private String remark;

	private List<ObjectMap> roleMenus;

	public Role() {
	}

	@Column(name = "name_", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Transient
	public List<ObjectMap> getRoleMenus() {
		return roleMenus;
	}

	public void setRoleMenus(List<ObjectMap> roleMenus) {
		this.roleMenus = roleMenus;
	}
}