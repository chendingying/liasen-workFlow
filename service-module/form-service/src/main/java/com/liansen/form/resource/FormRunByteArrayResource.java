package com.liansen.form.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liansen.common.constant.CoreConstant;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.ByteArray;
import com.liansen.form.domain.FormDefinition;
import com.liansen.form.domain.RunByteArray;
import com.liansen.form.inherit.InheritRunByteArray;
import com.liansen.form.repository.ByteArrayRepository;
import com.liansen.form.repository.FormDefinitionRepository;
import com.liansen.form.repository.RunByteArrayRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * Created by CDZ on 2018/9/15.
 */
@Api(description = "运行时的表单接口")
@RestController
public class FormRunByteArrayResource  extends BaseResource {
    @Autowired
    RunByteArrayRepository runByteArrayRepository;
    @Autowired
    FormDefinitionRepository formDefinitionRepository;

    @Autowired
    ByteArrayRepository byteArrayRepository;

    private RunByteArray getRunByteArrayFromRequest(Integer id) {
        RunByteArray runByteArray = runByteArrayRepository.findOne(id);
        if (runByteArray == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return runByteArray;
    }

    /**
     * 查询流程任务下的表单
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation("流程任务下的表单查询")
    @GetMapping("/form-definition/json")
    @ResponseStatus(HttpStatus.OK)
    public ObjectNode getFormdefinitionJson(@ApiIgnore @RequestParam Map<String, String> requestParams) throws Exception {
        RunByteArray runByteArray = null;
        if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
            runByteArray = runByteArrayRepository.findByProcInstId(requestParams.get("processInstanceId"));
        }
        ObjectNode resultNode = objectMapper.createObjectNode();
        if (runByteArray == null) {
            FormDefinition formDefinition = null;
            ByteArray byteArray = null;
            if (ObjectUtils.isNotEmpty(requestParams.get("formKey"))) {
                formDefinition = formDefinitionRepository.findByKey(requestParams.get("formKey"));
                byteArray = byteArrayRepository.findOne(formDefinition.getDeploySourceId());
            }
            if (byteArray == null) {
                resultNode.putPOJO("json", objectMapper.createArrayNode().toString());
            } else {
                resultNode.putPOJO("json", new String(byteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
            }
            resultNode.putPOJO("bytearrayId",0);
        } else {
            resultNode.putPOJO("json", new String(runByteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
            resultNode.putPOJO("bytearrayId",runByteArray.getId());
        }
        return resultNode;
    }

    /**
     * 添加流程任务下的表单
     * @throws Exception
     */
    @ApiOperation("添加流程任务下的表单")
    @PostMapping("/form-definition/json")
    public void saveFormdefinitionJson(@RequestBody InheritRunByteArray inheritRunByteArray) throws Exception{
        RunByteArray runByteArray = runByteArrayRepository.findByProcInstId(inheritRunByteArray.getProcInstId());
        if(runByteArray == null){
            runByteArray = new RunByteArray();
        }
        runByteArray.setTableKey(inheritRunByteArray.getTableKey());
        runByteArray.setProcInstId(inheritRunByteArray.getProcInstId());
        runByteArray.setContentByte(inheritRunByteArray.getEditorJson().getBytes(CoreConstant.DEFAULT_CHARSET));
        runByteArrayRepository.save(runByteArray);
    }

}
