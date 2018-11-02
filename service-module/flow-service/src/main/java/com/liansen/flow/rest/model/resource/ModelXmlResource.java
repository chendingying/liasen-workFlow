package com.liansen.flow.rest.model.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liansen.flow.constant.ErrorConstant;
import com.liansen.flow.rest.model.CustomBpmnJsonConverter;
import com.liansen.flow.rest.model.CustomUserTaskJsonConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

/**
 *  获取模型XML
 * @author cdy
 * @create 2018/9/4
 */
@Api(description = "模型XML接口")
@RestController
public class ModelXmlResource extends BaseModelResource {

    @ApiOperation("获取模型XML")
    @GetMapping(value = "/models/{modelId}/xml", name = "获取模型XML")
    public ResponseEntity<byte[]> getModelXml(@PathVariable String modelId) {
//        Model model = getModelFromRequest(modelId);
        try {

            //获取模型
            Model modelData = repositoryService.getModel(modelId);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            CustomBpmnJsonConverter.getConvertersToBpmnMap().put("UserTask", CustomUserTaskJsonConverter.class);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            BpmnModel model = jsonConverter.convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.TEXT_XML);
            return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), responseHeaders, HttpStatus.OK);


//            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
//            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
//            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
//            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
//            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
//            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.setContentType(MediaType.TEXT_XML);
//            return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("获取模型XML信息异常",e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_XML_READ_ERROR, e.getMessage());
        }
        return null;
    }
}
