package com.liansen.form.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.FormFieldType;
import com.liansen.form.repository.FormFieldTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;
import java.util.UUID;

/**
 * Created by CDZ on 2018/9/21.
 */
@Api(description = "表单字段类型接口")
@RestController
public class FormFieldTypeResource extends BaseResource {
    @Autowired
    FormFieldTypeRepository formFieldTypeRepository;

    private FormFieldType getFormFieldTypeFromRequest(Integer id) {
        FormFieldType formFieldType = formFieldTypeRepository.findOne(id);
        if (formFieldType == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formFieldType;
    }

    @ApiOperation("表单字段类型查询")
    @GetMapping(value = "/form-fieldTypes")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormFieldTypes(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<FormFieldType> criteria = new Criteria<FormFieldType>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("name", requestParams.get("name")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formFieldTypeRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation("根据表单字段类型Id查询")
    @GetMapping(value = "/form-fieldTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormFieldType getFormFieldType(@PathVariable Integer id) {
        return getFormFieldTypeFromRequest(id);
    }

    @ApiOperation("添加表单字段类型")
    @PostMapping("/form-fieldTypes")
    public void createFormFieldType(@RequestBody FormFieldType formFieldTypeRequest) {
        FormFieldType formFieldType = formFieldTypeRepository.findByName(formFieldTypeRequest.getName());
        if(formFieldType == null){
            FormFieldType type = new FormFieldType();
            type.setName(formFieldTypeRequest.getName());
            type.setKey(UUID.randomUUID().toString());
             formFieldTypeRepository.save(type);
        }
}
    @ApiOperation("修改表单字段类型")
    @PutMapping(value = "/form-fieldTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormFieldType updateFormFieldType(@PathVariable Integer id, @RequestBody FormFieldType formFieldTypeRequest) {
        FormFieldType formFieldType = getFormFieldTypeFromRequest(id);
        formFieldType.setName(formFieldTypeRequest.getName());
        formFieldType.setRemark(formFieldTypeRequest.getRemark());
        return formFieldTypeRepository.save(formFieldType);
    }

    @ApiOperation("删除表单字段类型")
    @DeleteMapping(value = "/form-fieldTypes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormFieldType(@PathVariable Integer id) {
        FormFieldType formFieldType = getFormFieldTypeFromRequest(id);
        formFieldTypeRepository.delete(formFieldType);
    }
}
