package com.liansen.flow.rest.model.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liansen.common.constant.CoreConstant;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.flow.cmd.SaveModelEditorCmd;
import com.liansen.flow.cmd.UpdateModelKeyCmd;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.constant.TableConstant;
import com.liansen.flow.rest.model.ModelEditorJsonRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 模型设计器接口
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "模型设计接口")
@RestController
public class ModelEditorResource extends BaseModelResource {

    @ApiOperation("设计器获取模型信息")
    @GetMapping(value = "/models/{modelId}/editor", name = "设计器获取模型信息")
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;
        Model model = getModelFromRequest(modelId);
        try {
            if (ObjectUtils.isNotEmpty(model.getMetaInfo())) {
                modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            } else {
                modelNode = objectMapper.createObjectNode();
                modelNode.put("name", model.getName());
            }
            modelNode.put("key", model.getKey());
            modelNode.put("category", model.getCategory());
            modelNode.put("tenantId", model.getTenantId());
            modelNode.put("modelId", model.getId());
            byte[] editors = repositoryService.getModelEditorSource(model.getId());
            ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(editors, CoreConstant.DEFAULT_CHARSET));
            editorJsonNode.put("modelType", "model");
            modelNode.set("model", editorJsonNode);
        } catch (Exception e) {
            logger.error("获取模型设计信息异常", e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_GET_EDITOR_ERROR, e.getMessage());
        }
        return modelNode;
    }

    @ApiOperation("模型设计器保存模型")
    @PostMapping(value = "/models/{modelId}/editor", name = "模型设计器保存模型")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModelEditor(@PathVariable String modelId, @RequestBody ModelEditorJsonRequest values) {

         String json =  values.getJsonXml();

        Model model = getModel(modelId, values.isNewVersion());
        if(!model.getKey().equals(values.getKey())) {
            checkModelKeyExists(values.getKey());
            managementService.executeCommand(new UpdateModelKeyCmd(modelId, values.getKey()));
        }
        try {
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put("name", values.getName());
            modelJson.put("description", values.getDescription());
            model.setMetaInfo(modelJson.toString());
            model.setName(values.getName());
            model.setKey(values.getKey());
            repositoryService.saveModel(model);
            managementService.executeCommand(new SaveModelEditorCmd(model.getId(), values.getJsonXml()));
        } catch (Exception e) {
            logger.error("保存模型设计信息异常", e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_GET_EDITOR_ERROR, e.getMessage());
        }
        loggerConverter.save("保存模型设计 '" + model.getName() + "'");
    }

    private Model getModel(String modelId, boolean isNewVersion) {
        Model model = getModelFromRequest(modelId);
        if (isNewVersion) {
            Model newModel = repositoryService.newModel();
            Model lastModel = repositoryService.createModelQuery().modelKey(model.getKey()).latestVersion().singleResult();
            if (lastModel == null) {
                newModel.setVersion(TableConstant.MODEL_VESION_START);
            } else {
                newModel.setVersion(lastModel.getVersion() + 1);
            }
            newModel.setKey(model.getKey());
            newModel.setMetaInfo(model.getMetaInfo());
            newModel.setCategory(model.getCategory());
            newModel.setTenantId(model.getTenantId());
            return newModel;
        } else {
            return model;
        }
    }
}
