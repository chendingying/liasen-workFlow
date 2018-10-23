package com.liansen.form.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.FormDefinition;
import com.liansen.form.domain.FormLayout;
import com.liansen.form.domain.FormTable;
import com.liansen.form.repository.FormDefinitionRepository;
import com.liansen.form.repository.FormLayoutRepository;
import com.liansen.form.repository.FormTableRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 数据表资源类
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "表单实体接口")
@RestController
public class FormTableResource extends BaseResource {
    @Autowired
    private FormTableRepository formTableRepository;
    @Autowired
    private FormLayoutRepository formLayoutRepository;

    @Autowired
    private FormDefinitionRepository formDefinitionRepository;

    private FormTable getFormTableFromRequest(Integer id) {
        FormTable formTable = formTableRepository.findOne(id);
        if (formTable == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formTable;
    }

    @ApiOperation("表单实体查询")
    @GetMapping(value = "/form-tables")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormTables(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<FormTable> criteria = new Criteria<FormTable>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("category", requestParams.get("category")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formTableRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation("根据表单实体Id查询")
    @GetMapping(value = "/form-tables/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormTable getFormTable(@PathVariable Integer id) {
        return getFormTableFromRequest(id);
    }

    @ApiOperation("添加表单实体")
    @PostMapping("/form-tables")
    @ResponseStatus(HttpStatus.CREATED)
    public FormTable createFormTable(@RequestBody FormTable formTableRequest) {
        formTableRequest.setKey("sid-"+ UUID.randomUUID().toString());
        return formTableRepository.save(formTableRequest);
    }

    @ApiOperation("修改表单实体")
    @PutMapping(value = "/form-tables/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormTable updateFormTable(@PathVariable Integer id, @RequestBody FormTable formTableRequest) {
        FormTable formTable = getFormTableFromRequest(id);
        formTable.setName(formTableRequest.getName());
        formTable.setRemark(formTableRequest.getRemark());
        formTable.setTenantId(formTableRequest.getTenantId());
        return formTableRepository.save(formTable);
    }

    @ApiOperation("删除表单实体")
    @DeleteMapping(value = "/form-tables/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormTable(@PathVariable Integer id) {
        FormTable formTable = getFormTableFromRequest(id);
        formTableRepository.delete(formTable);
    }

    @ApiOperation("部署表单实体")
    //部署(参数说明：表单编号)
    @PutMapping(value = "/form-tables/{tableId}/deploy")
    @ResponseStatus(value = HttpStatus.OK)
    public FormTable deploy(@PathVariable Integer tableId) {
        FormTable formTable = getFormTableFromRequest(tableId);
        List<FormLayout> formLayoutList = formLayoutRepository.findByTableId(tableId);

        if(formTable == null){
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }if(formLayoutList == null){
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_LAYOUT_NOT_FOUND);
        }
        for(FormLayout formLayout : formLayoutList){
            FormDefinition definition = formDefinitionRepository.findByKeyAndTableId(formTable.getKey(),formTable.getId());
            if(definition == null){
                definition = new FormDefinition();
            }
            definition.setTableId(formTable.getId());
            definition.setKey(formTable.getKey());
            definition.setName(formTable.getName());
            definition.setCategory(formTable.getCategory());
            definition.setDeploySourceId(formLayout.getEditorSourceId());
            definition.setRemark(formTable.getRemark());
            formDefinitionRepository.save(definition);
        }
        return formTable;
    }

}
