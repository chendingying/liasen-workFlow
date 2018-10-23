package com.liansen.identity.domain;

import com.liansen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_id_role_menu database table.
 * 
 */
@Entity
@Table(name="workflow_role_menu", catalog="ksh")
@NamedQuery(name="RoleMenu.findAll", query="SELECT r FROM RoleMenu r")
public class RoleMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("菜单Id")
	private int menuId;

	@ApiModelProperty("角色Id")
	private int roleId;

	public RoleMenu() {
	}

	@Column(name="menu_id_", nullable=false)
	public int getMenuId() {
		return this.menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}


	@Column(name="role_id_", nullable=false)
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}



}