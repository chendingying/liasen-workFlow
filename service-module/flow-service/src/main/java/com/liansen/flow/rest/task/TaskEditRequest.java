package com.liansen.flow.rest.task;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


public class TaskEditRequest {
	@ApiModelProperty("节点定义名称")
	private String name;

	@ApiModelProperty("签收人或委托人")
	private String assignee;

	@ApiModelProperty("实际签收人")
	private String owner;

	@ApiModelProperty("到期时间")
	private Date dueDate;

	private String category;

	@ApiModelProperty("节点定义描述")
	private String description;

	@ApiModelProperty("优先级别")
	private int priority;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
