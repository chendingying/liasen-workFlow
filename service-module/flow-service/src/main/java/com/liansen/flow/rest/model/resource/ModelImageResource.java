package com.liansen.flow.rest.model.resource;

import com.liansen.flow.constant.ErrorConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "获取模型流程图接口")
@RestController
public class ModelImageResource extends BaseModelResource {

    @ApiOperation("根据模型Id获取模型流程图")
    @GetMapping(value = "/models/{modelId}/image", name = "获取模型流程图")
    public ResponseEntity<byte[]> getModelImage(@PathVariable String modelId) throws UnsupportedEncodingException {
        Model model = getModelFromRequest(modelId);
        byte[] imageBytes = repositoryService.getModelEditorSourceExtra(model.getId());
        if (imageBytes == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.MODEL_IMAGE_NOT_FOUND, model.getId());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_PNG);
        try {
            return new ResponseEntity<byte[]>(imageBytes, responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_IMAGE_READ_ERROR, e.getMessage());
        }
        return null;
    }
}
