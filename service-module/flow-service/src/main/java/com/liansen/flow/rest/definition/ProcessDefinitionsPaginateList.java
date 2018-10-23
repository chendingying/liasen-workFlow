package com.liansen.flow.rest.definition;


import com.liansen.flow.rest.AbstractPaginateList;
import com.liansen.flow.rest.RestResponseFactory;

import java.util.List;


public class ProcessDefinitionsPaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public ProcessDefinitionsPaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createProcessDefinitionResponseList(list);
	}
}
