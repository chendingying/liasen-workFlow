package com.liansen.identity.domain;

import com.liansen.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_id_user_group database table.
 * 
 */
@Entity
@Table(name = "workflow_user_group", catalog="ksh")
@NamedQuery(name = "UserGroup.findAll", query = "SELECT s FROM UserGroup s")
public class UserGroup extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int groupId;
	private int userId;

	public UserGroup() {
	}

	@Column(name = "group_id_", nullable = false)
	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Column(name = "user_id_", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}