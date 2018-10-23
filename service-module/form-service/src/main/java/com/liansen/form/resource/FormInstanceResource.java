package com.liansen.form.resource;


import com.liansen.common.jpa.Criteria;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.FormDefinition;
import com.liansen.form.domain.FormInstance;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.form.repository.FormInstanceRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author cdy
 * @create 2018/9/12
 */
@Api(description = "表单实例接口")
@RestController
public class FormInstanceResource extends BaseResource {

    @Autowired
    FormInstanceRepository formInstanceRepository;
    private FormInstance getFormInstanceFromRequest(Integer id) {
        FormInstance formInstance = formInstanceRepository.findOne(id);
        if (formInstance == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return formInstance;
    }

    @ApiOperation("表单实例查询")
    @GetMapping(value = "/form-instance")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getFormDefinition(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<FormInstance> criteria = new Criteria<FormInstance>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("tableId", requestParams.get("tableId")));
        criteria.add(Restrictions.like("key", requestParams.get("key")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("remark", requestParams.get("remark")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(formInstanceRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation("根据表单实例Id查询")
    @GetMapping(value = "/form-instance/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormInstance getFormInstance(@PathVariable Integer id) {
        return getFormInstanceFromRequest(id);
    }

    @ApiOperation("修改表单实例")
    @PutMapping(value = "/form-instance/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public FormInstance updateFormInstance(@PathVariable Integer id, @RequestBody FormInstance formInstanceRequest) {
        FormInstance formInstance = getFormInstanceFromRequest(id);
        formInstance.setTableRelationId(formInstanceRequest.getTableRelationId());
        formInstance.setFormDefinitionId(formInstanceRequest.getFormDefinitionId());
        formInstance.setSuspensionState(formInstanceRequest.getSuspensionState());
        formInstance.setRelationTable(formInstanceRequest.getRelationTable());
        return formInstanceRepository.save(formInstance);
    }

    @ApiOperation("删除表单实例")
    @DeleteMapping(value = "/form-instance/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFormInstance(@PathVariable Integer id) {
        FormInstance formInstance = getFormInstanceFromRequest(id);
        formInstanceRepository.delete(formInstance);
    }

    @ApiOperation("启动表单实例")
    //启动实例
    @PostMapping(value = "/form-instance")
    public void startFormInstance(@RequestBody FormDefinition formDefinition){
        FormInstance formInstance = new FormInstance();
        formInstance.setTableRelationId(formDefinition.getTableId());
        formInstance.setFormDefinitionId(formDefinition.getId());
        formInstance.setSuspensionState(formDefinition.getSuspensionState());
        formInstance.setRev(formDefinition.getRev());
        formInstance.setRelationTable("");
        formInstanceRepository.save(formInstance);
    }
}
