package com.liansen.flow.rest.instance.resource;

import com.liansen.flow.constant.ErrorConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "流程实例激活接口")
@RestController
public class ProcessInstanceActivateResource extends BaseProcessInstanceResource {

	@ApiOperation("根据流程实例ID激活")
	@PutMapping(value = "/process-instances/{processInstanceId}/activate", name="流程实例激活")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional
	public void activateProcessInstance(@PathVariable String processInstanceId) {

		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		    
		if (!processInstance.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.INSTANCE_ALREADY_ACTIVE, processInstance.getId());
		}
		runtimeService.activateProcessInstanceById(processInstance.getId());
		loggerConverter.save("激活了流程实例 '"+processInstance.getProcessDefinitionName()+"'");
	}
}
