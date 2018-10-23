package com.liansen.flow.rest.model.resource;

import com.liansen.common.utils.ObjectUtils;
import com.liansen.flow.constant.TableConstant;
import com.liansen.flow.rest.model.ModelRequest;
import com.liansen.flow.rest.model.ModelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 模型复制接口
 *
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "模型复制接口")
@RestController
public class ModelCopyResource extends BaseModelResource {

    @ApiOperation("模型模型Id复制模型")
    @PostMapping(value = "/models/{modelId}/copy", name = "模型复制")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED)
    public ModelResponse copyModel(@PathVariable String modelId, @RequestBody ModelRequest modelRequest) {
        checkModelKeyExists(modelRequest.getKey());
        Model model = getModelFromRequest(modelId);

        Model newModel = repositoryService.newModel();
        newModel.setKey(modelRequest.getKey());
        newModel.setVersion(TableConstant.MODEL_VESION_START);

        if (ObjectUtils.isNotEmpty(modelRequest.getName())) {
            newModel.setName(modelRequest.getName());
        } else {
            newModel.setName(model.getName());
        }
        if (ObjectUtils.isNotEmpty(modelRequest.getCategory())) {
            newModel.setCategory(modelRequest.getCategory());
        } else {
            newModel.setCategory(model.getCategory());
        }
        if (ObjectUtils.isNotEmpty(modelRequest.getMetaInfo())) {
            newModel.setMetaInfo(modelRequest.getMetaInfo());
        } else {
            newModel.setMetaInfo(model.getMetaInfo());
        }
        if (ObjectUtils.isNotEmpty(modelRequest.getTenantId())) {
            newModel.setTenantId(modelRequest.getTenantId());
        } else {
            newModel.setTenantId(model.getTenantId());
        }
        repositoryService.saveModel(newModel);
        repositoryService.addModelEditorSource(newModel.getId(), repositoryService.getModelEditorSource(modelId));
        repositoryService.addModelEditorSourceExtra(newModel.getId(), repositoryService.getModelEditorSourceExtra(modelId));
        loggerConverter.save("复制了模型 '" + model.getName() + "'");
        return restResponseFactory.createModelResponse(newModel);
    }
}
