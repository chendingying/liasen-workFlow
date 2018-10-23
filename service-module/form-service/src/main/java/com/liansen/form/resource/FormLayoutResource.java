package com.liansen.form.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liansen.common.constant.CoreConstant;
import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.ByteArray;
import com.liansen.form.domain.FormField;
import com.liansen.form.domain.FormLayout;
import com.liansen.form.repository.ByteArrayRepository;
import com.liansen.form.repository.FormDefinitionRepository;
import com.liansen.form.repository.FormFieldRepository;
import com.liansen.form.repository.FormLayoutRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 数据表资源类
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "表单模型接口")
@RestController
public class FormLayoutResource extends BaseResource {
    @Autowired
    private FormLayoutRepository formLayoutRepository;
    @Autowired
    private FormFieldRepository formFieldRepository;
    @Autowired
    private ByteArrayRepository byteArrayRepository;
    @Autowired
    private FormDefinitionRepository formDefinitionRepository;

    private FormLayout getFormLayoutFromRequest(Integer id) {
        FormLayout formLayout = formLayoutRepository.findOne(id);
        if (formLayout == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formLayout;
    }

    @ApiOperation("表单模型查询")
    @GetMapping(value = "/form-layouts")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormLayouts(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<FormLayout> criteria = new Criteria<FormLayout>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formLayoutRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation("根据表单模型Id查询")
    @GetMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormLayout getFormLayout(@PathVariable Integer id) {
        return getFormLayoutFromRequest(id);
    }

    @ApiOperation("添加表单模型")
    @PostMapping("/form-layouts")
    @ResponseStatus(HttpStatus.CREATED)
    public FormLayout createFormLayout(@RequestBody FormLayout formLayoutRequest) {
        return formLayoutRepository.save(formLayoutRequest);
    }

    @ApiOperation("修改表单模型")
    @PutMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormLayout updateFormLayout(@PathVariable Integer id, @RequestBody FormLayout formLayoutRequest) {
        FormLayout formLayout = getFormLayoutFromRequest(id);
        formLayout.setName(formLayoutRequest.getName());
        formLayout.setTenantId(formLayoutRequest.getTenantId());
        return formLayoutRepository.save(formLayout);
    }

    @ApiOperation("删除表单模型")
    @DeleteMapping(value = "/form-layouts/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormLayout(@PathVariable Integer id) {
        FormLayout formLayout = getFormLayoutFromRequest(id);
        formLayoutRepository.delete(formLayout);
    }

    @ApiOperation("根据表单模型Id查询json串")
    @GetMapping("/form-layouts/{id}/json")
    @ResponseStatus(HttpStatus.OK)
    public ObjectNode getFormLayoutJson(@PathVariable Integer id) throws Exception {
        FormLayout formLayout = getFormLayoutFromRequest(id);

        List<FormField> formFields = formFieldRepository.findByTableId(formLayout.getTableId());

        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.putPOJO("fields", formFields);

        ByteArray byteArray = byteArrayRepository.findOne(formLayout.getEditorSourceId());
        if(byteArray == null) {
            resultNode.putPOJO("json", objectMapper.createArrayNode().toString());
        }else {
            resultNode.putPOJO("json", new String(byteArray.getContentByte(), CoreConstant.DEFAULT_CHARSET));
        }

        return resultNode;
    }

    @ApiOperation("修改表单模型json")
    @PutMapping("/form-layouts/{id}/json")
    @ResponseStatus(HttpStatus.OK)
    public void saveFormLayoutJson(@PathVariable Integer id,@RequestBody String editorJson) throws Exception {
        FormLayout formLayout = getFormLayoutFromRequest(id);

        ByteArray byteArray = byteArrayRepository.findOne(formLayout.getEditorSourceId());
        if(byteArray == null) {
            byteArray = new ByteArray();
            byteArray.setName(formLayout.getName());
        }

        byteArray.setContentByte(editorJson.getBytes(CoreConstant.DEFAULT_CHARSET));
        byteArrayRepository.save(byteArray);

        formLayout.setEditorSourceId(byteArray.getId());
        formLayoutRepository.save(formLayout);
    }



}
