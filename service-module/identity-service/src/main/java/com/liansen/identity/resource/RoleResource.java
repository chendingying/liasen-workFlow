package com.liansen.identity.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.model.ObjectMap;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.identity.constant.ErrorConstant;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.Menu;
import com.liansen.identity.domain.Role;
import com.liansen.identity.domain.RoleMenu;
import com.liansen.identity.domain.User;
import com.liansen.identity.repository.*;
import com.liansen.identity.response.ConvertFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 角色资源控制类
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "角色接口")
@RestController
public class RoleResource extends BaseResource {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private Role getRoleFromRequest(Integer id) {
        Role role = roleRepository.findOne(id);
        if (role == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.ROLE_NOT_FOUND);
        }
        return role;
    }

    @ApiOperation(value = "角色查询" , httpMethod = "GET")
    @GetMapping(value = "/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getRoles(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<Role> criteria = new Criteria<Role>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        return createPageResponse(roleRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation(value = "根据角色Id查询" , httpMethod = "GET")
    @GetMapping(value = "/roles/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Role getRole(@PathVariable Integer id) {
        return getRoleFromRequest(id);
    }

    @ApiOperation(value = "角色权限菜单查询" , httpMethod = "GET")
    @GetMapping(value = "/roles/menus")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getRoleMenus(@RequestParam(required = false) Integer id) {
        List<Menu> roleMenus = null;
        List<Menu> allMenus = menuRepository.findByStatusOrderByOrderAsc(TableConstant.MENU_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleMenus = menuRepository.findByRoleId(id);
        }
        return ConvertFactory.convertRoleMenus(allMenus, roleMenus);
    }

    @ApiOperation(value = "添加角色信息" , httpMethod = "POST")
    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Role createRole(@RequestBody Role roleRequest) {
        return saveRoleAndMenu(null, roleRequest);
    }

    @ApiOperation(value = "修改角色信息" , httpMethod = "PUT")
    @PutMapping(value = "/roles/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public Role updateRole(@PathVariable Integer id, @RequestBody Role roleRequest) {
        Role role = getRoleFromRequest(id);
        return saveRoleAndMenu(role, roleRequest);
    }

    private Role saveRoleAndMenu(Role role, Role roleRequest) {
        if (role == null) {
            role = new Role();
        }
        role.setName(roleRequest.getName());
        roleRepository.save(role);

        return role;
    }

    @ApiOperation(value = "根据角色Id查询关联用户" , httpMethod = "GET")
    @GetMapping(value = "/roles/{id}/users")
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getRoleUsers(@PathVariable Integer id) {
        return userRepository.findByRoleId(id);
    }

    @ApiOperation(value = "删除角色下的用户信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/roles/{id}/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteRoleUser(@PathVariable Integer id, @PathVariable(value = "userId") Integer userId) {
        userRoleRepository.deleteByRoleIdAndUserId(id, userId);
    }

    @ApiOperation(value = "删除角色信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/roles/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteRole(@PathVariable Integer id) {
        Role role = getRoleFromRequest(id);
        List<User> users = userRepository.findByRoleId(role.getId());
        if (ObjectUtils.isNotEmpty(users)) {
            exceptionFactory.throwForbidden(ErrorConstant.ROLE_ALREADY_USER_USE, users.get(0).getName());
        }
        roleRepository.delete(role);
        roleMenuRepository.deleteByRoleId(role.getId());
    }

    @ApiOperation(value = "修改角色状态信息" , httpMethod = "PUT")
    @PutMapping(value = "/roles/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public Role switchStatus(@PathVariable Integer id) {
        Role role = getRoleFromRequest(id);
        if (role.getStatus() == TableConstant.ROLE_STATUS_NORMAL) {
            role.setStatus(TableConstant.ROLE_STATUS_STOP);
        } else {
            role.setStatus(TableConstant.ROLE_STATUS_NORMAL);
        }
        return roleRepository.save(role);
    }
}
