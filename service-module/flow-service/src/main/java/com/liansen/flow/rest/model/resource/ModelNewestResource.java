package com.liansen.flow.rest.model.resource;

import com.liansen.flow.constant.TableConstant;
import com.liansen.flow.rest.model.ModelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模型升级最新版
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "模型升级接口")
@RestController
public class ModelNewestResource extends BaseModelResource {

    @ApiOperation("模型作为最新版")
    @PostMapping(value = "/models/{modelId}/newest", name = "模型作为最新版")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED)
    public ModelResponse copyModel(@PathVariable String modelId) {
        Model model = getModelFromRequest(modelId);
        Model newModel = repositoryService.newModel();
        newModel.setKey(model.getKey());
        newModel.setName(model.getName());
        newModel.setCategory(model.getCategory());
        newModel.setMetaInfo(model.getMetaInfo());
        Model lastModel = repositoryService.createModelQuery().modelKey(newModel.getKey()).latestVersion().singleResult();
        if (lastModel == null) {
            newModel.setVersion(TableConstant.MODEL_VESION_START);
        } else {
            newModel.setVersion(lastModel.getVersion() + 1);
        }

        newModel.setTenantId(model.getTenantId());

        repositoryService.saveModel(newModel);

        repositoryService.addModelEditorSource(newModel.getId(), repositoryService.getModelEditorSource(modelId));
        repositoryService.addModelEditorSourceExtra(newModel.getId(), repositoryService.getModelEditorSourceExtra(modelId));
        return restResponseFactory.createModelResponse(newModel);
    }
}
