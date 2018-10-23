package com.liansen.identity.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "admin_users", catalog="ksh")
@NamedQuery(name = "User.findAll", query = "SELECT s FROM User s")
public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	private Integer id;

	@ApiModelProperty("登录帐号")
	private String userName;

	@ApiModelProperty("头像url")
	private String avatar;

	@ApiModelProperty("用户名")
	private String name;

	@ApiModelProperty("密码")
	private String password;

	@ApiModelProperty("token")
	private String rememberToken;

	@ApiModelProperty("创建时间")
	private Timestamp createdAt;

	@ApiModelProperty("修改时间")
	private Timestamp updatedAt;
//
//	@ApiModelProperty("用户角色")
//	private List<ObjectMap> userRoles;
//
//	@ApiModelProperty("用户分组")
//	private List<ObjectMap> userGroups;

	public User() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "avatar")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "remember_token")
	public String getRememberToken() {
		return rememberToken;
	}

	public void setRememberToken(String rememberToken) {
		this.rememberToken = rememberToken;
	}

	@Column(name = "created_at")
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Column(name = "updated_at")
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}