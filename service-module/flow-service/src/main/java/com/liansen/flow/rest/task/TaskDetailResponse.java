package com.liansen.flow.rest.task;

import org.flowable.task.api.DelegationState;

public class TaskDetailResponse extends HistoricTaskResponse {
	private String delegationState;
	private boolean suspended;
	protected String ownerName;
	protected String assigneeName;
	protected String  form_update;


	public String getDelegationState() {
		return delegationState;
	}

	public void setDelegationState(String delegationState) {
		this.delegationState = delegationState;
	}
	public void setDelegationState(DelegationState delegationState) {
		this.delegationState = getDelegationStateString(delegationState);
	}
	
	private String getDelegationStateString(DelegationState state) {
		String result = null;
	    if(state != null) {
	      result = state.toString().toLowerCase();
	    }
	    return result;
	}
	
	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getForm_update() {
		return form_update;
	}

	public void setForm_update(String form_update) {
		this.form_update = form_update;
	}
}
