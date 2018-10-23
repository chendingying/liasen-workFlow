package com.liansen.flow.rest.instance;


import com.liansen.flow.rest.AbstractPaginateList;
import com.liansen.flow.rest.RestResponseFactory;

import java.util.List;

public class ProcessInstancePaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public ProcessInstancePaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createHistoricProcessInstancResponseList(list);
	}
}
