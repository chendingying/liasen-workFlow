package com.liansen.flow.rest.model.resource;

import com.liansen.flow.cmd.DeployModelCmd;
import com.liansen.flow.rest.definition.ProcessDefinitionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模型部署
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "模型部署接口")
@RestController
public class ModelDeployResource extends BaseModelResource {

    @ApiOperation("根据模型ID部署")
    @PostMapping(value = "/models/{modelId}/deploy", name = "模型部署")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional(propagation = Propagation.REQUIRED)
    public ProcessDefinitionResponse deployModel(@PathVariable String modelId) {
        Model model = getModelFromRequest(modelId);
        Deployment deployment = managementService.executeCommand(new DeployModelCmd(model.getId()));

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        loggerConverter.save("部署模型 '" + model.getName() + "'");
        return restResponseFactory.createProcessDefinitionResponse(processDefinition);
    }
}
