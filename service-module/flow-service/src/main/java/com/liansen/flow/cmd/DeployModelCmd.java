package com.liansen.flow.cmd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liansen.flow.rest.model.CustomBpmnJsonConverter;
import com.liansen.flow.rest.model.CustomUserTaskJsonConverter;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.Model;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * 部署模型
 * @author cdy
 * @create 2018/9/4
 */
public class DeployModelCmd implements Command<Deployment>, Serializable {
    private static final long serialVersionUID = 1L;
    private String modelId;

    public DeployModelCmd(String modelId) {
        this.modelId = modelId;
    }

    public Deployment execute(CommandContext commandContext) {
        Deployment deployment = null;

        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration(commandContext).getRepositoryService();
        Model model = repositoryService.getModel(modelId);
        if (model == null) {
            throw new FlowableObjectNotFoundException("Could not find a model with id '" + modelId + "'.", Model.class);
        }

        byte[] editorSource = CommandContextUtil.getProcessEngineConfiguration(commandContext).getModelEntityManager().findEditorSourceByModelId(modelId);
        if (editorSource == null) {
            throw new FlowableObjectNotFoundException("Model with id '" + modelId + "' does not have source available.", String.class);
        }

        try {
            //获取模型
            Model modelData = repositoryService.getModel(modelId);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            CustomBpmnJsonConverter.getConvertersToBpmnMap().put("UserTask", CustomUserTaskJsonConverter.class);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
            String fileName = modelData.getName() + ".bpmn20.xml";
            ByteArrayInputStream bis = new ByteArrayInputStream(bpmnBytes);
            deploymentBuilder.addInputStream(fileName, bis);
            deploymentBuilder.name(fileName);
            // modelId设置为部署的分类字段作为后续关联的需要
            deploymentBuilder.category(modelData.getId());

            if (modelData.getTenantId() != null) {
                deploymentBuilder.tenantId(modelData.getTenantId());
            }
            //部署
            deployment = deploymentBuilder.deploy();
            modelData.setDeploymentId(deployment.getId());
        } catch (Exception e) {
            if (e instanceof FlowableException) {
                throw (FlowableException) e;
            }
            throw new FlowableException(e.getMessage(), e);
        }

        return deployment;
    }
}
