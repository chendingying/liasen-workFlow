package com.liansen.flow.rest.task;


import com.liansen.flow.rest.variable.RestVariable;

import java.util.List;


public class TaskCompleteRequest {

	private List<RestVariable> variables;

	public List<RestVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<RestVariable> variables) {
		this.variables = variables;
	}
	
}
