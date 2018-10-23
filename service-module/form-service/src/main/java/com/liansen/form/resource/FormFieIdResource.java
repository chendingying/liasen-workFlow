package com.liansen.form.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.FormField;
import com.liansen.form.repository.FormFieldRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 数据表资源类
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "表单字段接口")
@RestController
public class FormFieIdResource extends BaseResource {
    @Autowired
    private FormFieldRepository formFieldRepository;

    private FormField getFormFieldFromRequest(Integer id) {
        FormField formField = formFieldRepository.findOne(id);
        if (formField == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formField;
    }

    @ApiOperation("表单字段查询")
    @GetMapping(value = "/form-fields")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormFields(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<FormField> criteria = new Criteria<FormField>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formFieldRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation("根据表单字段Id查询")
    @GetMapping(value = "/form-fields/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormField getFormField(@PathVariable Integer id) {
        return getFormFieldFromRequest(id);
    }

    @ApiOperation("添加表单字段")
    @PostMapping("/form-fields")
    @ResponseStatus(HttpStatus.CREATED)
    public FormField createFormField(@RequestBody FormField formFieldRequest) {
        return formFieldRepository.save(formFieldRequest);
    }

    @ApiOperation("修改表单字段")
    @PutMapping(value = "/form-fields/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormField updateFormField(@PathVariable Integer id, @RequestBody FormField formFieldRequest) {
        FormField formField = getFormFieldFromRequest(id);
        formField.setName(formFieldRequest.getName());
        formField.setType(formFieldRequest.getType());
        formField.setRemark(formFieldRequest.getRemark());
        return formFieldRepository.save(formField);
    }

    @ApiOperation("删除表单字段")
    @DeleteMapping(value = "/form-fields/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormField(@PathVariable Integer id) {
        FormField formField = getFormFieldFromRequest(id);
        formFieldRepository.delete(formField);
    }
}
