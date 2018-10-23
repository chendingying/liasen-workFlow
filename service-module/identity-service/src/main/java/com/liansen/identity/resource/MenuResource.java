package com.liansen.identity.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.identity.constant.ErrorConstant;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.Menu;
import com.liansen.identity.domain.Role;
import com.liansen.identity.repository.MenuRepository;
import com.liansen.identity.repository.RoleMenuRepository;
import com.liansen.identity.repository.RoleRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 菜单资源控制类
 * @author cdy
 * @create 2018/9/5
 */

@Api(description = "菜单接口")
@RestController
public class MenuResource extends BaseResource {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    private Menu getMenuFromRequest(Integer id) {
        Menu menu = menuRepository.findOne(id);
        if (menu == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.MENU_NOT_FOUND);
        }
        return menu;
    }

    @ApiOperation(value = "菜单查询" , httpMethod = "GET")
    @GetMapping(value = "/menus")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getMenus(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<Menu> criteria = new Criteria<Menu>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(menuRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation(value = "根据菜单Id查询" , httpMethod = "GET")
    @GetMapping(value = "/menus/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Menu getMenu(@PathVariable Integer id) {
        return getMenuFromRequest(id);
    }

    @ApiOperation(value = "添加菜单信息" , httpMethod = "POST")
    @PostMapping("/menus")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu createMenu(@RequestBody Menu menuRequest) {
        Menu menu = new Menu();
        menu.setRemark(menuRequest.getRemark());
        menu.setStatus(menuRequest.getStatus());
        menu.setIcon(menuRequest.getIcon());
        menu.setName(menuRequest.getName());
        menu.setOrder(menuRequest.getOrder());
        menu.setParentId(menuRequest.getParentId());
        menu.setRoute(menuRequest.getRoute());
        menu.setType(menuRequest.getType());
        return menuRepository.save(menu);
    }

    @ApiOperation(value = "修改菜单信息" , httpMethod = "PUT")
    @PutMapping(value = "/menus/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Menu updateMenu(@PathVariable Integer id, @RequestBody Menu menuRequest) {
        Menu menu = getMenuFromRequest(id);
        menu.setName(menuRequest.getName());
        menu.setStatus(menuRequest.getStatus());
        menu.setIcon(menuRequest.getIcon());
        menu.setOrder(menuRequest.getOrder());
        menu.setParentId(menuRequest.getParentId());
        menu.setType(menuRequest.getType());
        menu.setRoute(menuRequest.getRoute());
        menu.setRemark(menuRequest.getRemark());
        return menuRepository.save(menu);
    }

    @ApiOperation(value = "修改菜单状态" , httpMethod = "PUT")
    @PutMapping(value = "/menus/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public Menu switchStatus(@PathVariable Integer id) {
        Menu menu = getMenuFromRequest(id);
        if (menu.getStatus() == TableConstant.MENU_STATUS_NORMAL) {
            menu.setStatus(TableConstant.MENU_STATUS_STOP);
        } else {
            menu.setStatus(TableConstant.MENU_STATUS_NORMAL);
        }
        return menuRepository.save(menu);
    }

    @ApiOperation(value = "查询菜单下的角色" , httpMethod = "GET")
    @GetMapping(value = "/menus/{id}/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Role> getMenuRoles(@PathVariable Integer id) {
        return roleRepository.findByMenuId(id);
    }

    @ApiOperation(value = "删除菜单下的角色信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/menus/{id}/roles/{roleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteMenuRole(@PathVariable Integer id, @PathVariable(value = "roleId") Integer roleId) {
        roleMenuRepository.deleteByMenuIdAndRoleId(id, roleId);
    }

    @ApiOperation(value = "删除菜单信息" , httpMethod = "GET")
    @DeleteMapping(value = "/menus/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable Integer id) {
        Menu menu = getMenuFromRequest(id);
        if (menu.getType() == TableConstant.MENU_TYPE_PARENT) {
            List<Menu> children = menuRepository.findByParentId(menu.getId());
            if (ObjectUtils.isNotEmpty(children)) {
                exceptionFactory.throwForbidden(ErrorConstant.MENU_HAVE_CHILDREN);
            }
        } else {
            List<Role> roles = roleRepository.findByMenuId(menu.getId());
            if (ObjectUtils.isNotEmpty(roles)) {
                exceptionFactory.throwForbidden(ErrorConstant.MENU_ALREADY_ROLE_USE, roles.get(0).getName());
            }
        }
        menuRepository.delete(menu);
    }
}
