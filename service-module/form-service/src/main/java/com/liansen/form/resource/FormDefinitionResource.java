package com.liansen.form.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.FormDefinition;
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
 *
 * @author cdy
 * @create 2018/9/8
 */
@Api(description = "表单定义接口")
@RestController
public class FormDefinitionResource extends BaseResource {

    @Autowired
    FormDefinitionRepository formDefinitionRepository;
    @Autowired
    private ByteArrayRepository byteArrayRepository;

    @Autowired
    private RunByteArrayRepository runByteArrayRepository;

    private FormDefinition getFormDefinitionFromRequest(Integer id) {
        FormDefinition formDefinition = formDefinitionRepository.findOne(id);
        if (formDefinition == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formDefinition;
    }

    @ApiOperation("表单定义查询")
    @GetMapping(value = "/form-definition")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormDefinition(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<FormDefinition> criteria = new Criteria<FormDefinition>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("category",requestParams.get("category")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        return createPageResponse(formDefinitionRepository.findAll(criteria, getPageable(requestParams)));
    }

//    @ApiOperation("")
//    @GetMapping(value = "/form-definition/byte/{id}")
//    public PageResponse getFormDefinitionByteArray(@PathVariable Integer id){
////        List<FormDefinition> formDefinitions =  formDefinitionRepository.findFeploySourceIdByTableId(id);
//        return null;
//    }

    @ApiOperation("根据表单Id查询")
    @GetMapping(value = "/form-definition/{id}")
        @ResponseStatus(value = HttpStatus.OK)
        public FormDefinition getFormDefinition(@PathVariable Integer id) {
        return getFormDefinitionFromRequest(id);
    }

    @ApiOperation("修改表单定义信息")
    @PutMapping(value = "/form-definition/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormDefinition updateFormDefinition(@PathVariable Integer id, @RequestBody FormDefinition formDefinitionRequest) {
        return formDefinitionRepository.save(formDefinitionRequest);
    }

    @ApiOperation("删除表单定义信息")
    @DeleteMapping(value = "/form-definition/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormDefinition(@PathVariable Integer id) {
        FormDefinition formDefinition = getFormDefinitionFromRequest(id);
        formDefinitionRepository.delete(formDefinition);
    }

}
