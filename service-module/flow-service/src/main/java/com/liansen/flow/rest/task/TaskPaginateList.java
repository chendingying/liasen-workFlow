package com.liansen.flow.rest.task;



import com.liansen.flow.rest.AbstractPaginateList;
import com.liansen.flow.rest.RestResponseFactory;

import java.util.List;


public class TaskPaginateList extends AbstractPaginateList {

	protected RestResponseFactory restResponseFactory;

	public TaskPaginateList(RestResponseFactory restResponseFactory) {
		this.restResponseFactory = restResponseFactory;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List processList(List list) {
		return restResponseFactory.createHistoricTaskResponseList(list);
	}
}
