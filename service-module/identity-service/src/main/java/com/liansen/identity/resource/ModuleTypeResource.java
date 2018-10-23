package com.liansen.identity.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.identity.constant.ErrorConstant;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.ModuleType;
import com.liansen.identity.repository.ModuleTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * Created by CDZ on 2018/9/18.
 */
@Api(description = "分类接口")
@RestController
public class ModuleTypeResource extends BaseResource {

    @Autowired
    ModuleTypeRepository moduleTypeRepository;
    private ModuleType getModuleTypeRequest(Integer id){
        ModuleType moduleType = moduleTypeRepository.findOne(id);
        if(moduleType == null){
            exceptionFactory.throwObjectNotFound(ErrorConstant.MENU_NOT_FOUND);
        }
        return moduleType;
    }

    @ApiOperation(value = "添加分类信息" , httpMethod = "POST")
    @PostMapping(value = "/moduleTypes")
    public ModuleType save(@RequestBody ModuleType moduleType)
    {
        ModuleType module = moduleTypeRepository.findByModuleTypeAndParentId(moduleType.getModuleType(),moduleType.getParentId());
        if(module == null){
            return moduleTypeRepository.save(moduleType);
        }
        return null;
    }

    @ApiOperation(value = "分类查询" , httpMethod = "GET")
    @GetMapping(value = "/moduleTypes")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getModuleTypes(@ApiIgnore @RequestParam Map<String, String> requestParams){
        Criteria<ModuleType> criteria = new Criteria<ModuleType>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.like("moduleType", requestParams.get("moduleType")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        return createPageResponse(moduleTypeRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation(value = "根据分类Id查询" , httpMethod = "GET")
    @GetMapping(value = "/moduleTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ModuleType getModuleTypes(@PathVariable Integer id){
        return getModuleTypeRequest(id);
    }

    @ApiOperation(value = "根据分类父级Id查询" , httpMethod = "GET")
    @GetMapping(value = "/moduleTypes/parentId/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ModuleType> listModuleTypes(@PathVariable Integer id){return moduleTypeRepository.findByParentId(id);}

    @ApiOperation(value = "删除分类信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/moduleTypes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteModuleType(@PathVariable Integer id){
        ModuleType moduleType = getModuleTypeRequest(id);
        if(moduleType.getParentId() == TableConstant.MODULE_TYPE_PARNET_ID){
            List<ModuleType> list = moduleTypeRepository.findByParentId(moduleType.getId());
            if (ObjectUtils.isNotEmpty(list) || list.size() > 0) {
                exceptionFactory.throwForbidden(ErrorConstant.MODULETYPE_HAVE_CHILDREN);
            }
        }
        moduleTypeRepository.delete(moduleType);
    }

    @ApiOperation(value = "修改分类信息" , httpMethod = "PUT")
    @PutMapping(value = "/moduleTypes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ModuleType updateModuleType(@PathVariable Integer id,@RequestBody ModuleType moduleTypeRequest){
       ModuleType moduleType =  getModuleTypeRequest(id);
        moduleType.setModuleType(moduleTypeRequest.getModuleType());
        moduleType.setParentId(moduleTypeRequest.getParentId());
        moduleType.setRemark(moduleTypeRequest.getRemark());
        moduleType.setStatus(moduleTypeRequest.getStatus());
        return moduleTypeRepository.save(moduleType);
    }

    @ApiOperation(value = "修改分类状态" , httpMethod = "PUT")
    //修改类型状态
    @PutMapping(value = "/moduleTypes/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public ModuleType switchStatus(@PathVariable Integer id) {
        ModuleType moduleType = getModuleTypeRequest(id);
        if (moduleType.getStatus() == TableConstant.MODULE_TYPE_STATUS_NORMAL) {
            moduleType.setStatus(TableConstant.MODULE_TYPE_STATUS_STOP);
        } else {
            moduleType.setStatus(TableConstant.MODULE_TYPE_STATUS_NORMAL);
        }
        return moduleTypeRepository.save(moduleType);
    }
}
