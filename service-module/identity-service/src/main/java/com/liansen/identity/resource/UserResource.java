package com.liansen.identity.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.model.Authentication;
import com.liansen.common.model.ObjectMap;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.identity.constant.ErrorConstant;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.*;
import com.liansen.identity.oauth.CustomUserDetails;
import com.liansen.identity.repository.*;
import com.liansen.identity.response.ConvertFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ShellProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.annotations.ApiIgnore;
import sun.net.www.protocol.http.AuthenticationHeader;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 人员资源控制类
 * @author cdy
 * @create 2018/9/5
 */
@RestController
@Api(description = "用户接口")
public class UserResource extends BaseResource {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TokenStore tokenStore;
    private User getUserFromRequest(Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }
        return user;
    }

    @ApiOperation(value = "用户查询" , httpMethod = "GET")
    @GetMapping(value = "/users")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getUsers(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<User> criteria = new Criteria<User>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("phone", requestParams.get("phone")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(userRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation(value = "根据用户Id查询" , httpMethod = "GET")
    @GetMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public User getUser(@PathVariable Integer id) {
        return getUserFromRequest(id);
    }

    @ApiOperation(value = "查询用户角色信息" , httpMethod = "GET")
    @GetMapping(value = "/users/roles")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getUserRoles(@RequestParam(required = false) Integer id) {
        List<Role> roleRoles = null;
        List<Role> allRoles = roleRepository.findByStatus(TableConstant.ROLE_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleRoles = roleRepository.findByUserId(id);
        }
        return ConvertFactory.convertUseRoles(allRoles, roleRoles);
    }

    @ApiOperation(value = "查询用户分组信息" , httpMethod = "GET")
    @GetMapping(value = "/users/groups")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ObjectMap> getUserGroups(@RequestParam(required = false) Integer id) {
        List<Group> roleGroups = null;
        List<Group> allGroups = groupRepository.findByStatusOrderByOrderAsc(TableConstant.GROUP_STATUS_NORMAL);
        if (ObjectUtils.isNotEmpty(id)) {
            roleGroups = groupRepository.findByUserId(id);
        }
        return ConvertFactory.convertUserGroups(allGroups, roleGroups);
    }

    @ApiOperation(value = "添加用户信息" , httpMethod = "POST")
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public User createUser(@RequestBody User userRequest) {
        String account = userRequest.getUserName();
        User user = userRepository.findByUserName(account);
        if (user != null) {
            exceptionFactory.throwConflict(ErrorConstant.USER_ACCOUNT_REPEAT);
        }
        return saveUserAndGroupAndRole(null,userRequest);
    }

    @ApiOperation(value = "修改用户信息" , httpMethod = "PUT")
    @PutMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public User updateUser(@PathVariable Integer id, @RequestBody User userRequest) {
        User user = getUserFromRequest(id);
        return saveUserAndGroupAndRole(user, userRequest);
    }

    private User saveUserAndGroupAndRole(User user,User userRequest) {
        if (user == null) {
            user = new User();
            user.setUserName(userRequest.getUserName());
        }
        user.setName(userRequest.getName());
        userRepository.save(user);
        List<ObjectMap> roles = userRequest.getUserRoles();
        if(roles != null){
            userRoleRepository.deleteByUserId(user.getId());
            for (ObjectMap role : roles) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(role.getAsInteger("id"));
                userRole.setUserId(user.getId());
                userRoleRepository.save(userRole);
            }
        }
        List<ObjectMap> groups = userRequest.getUserGroups();
        if(groups != null){
            userGroupRepository.deleteByUserId(user.getId());
            for (ObjectMap group : groups) {
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(group.getAsInteger("id"));
                userGroup.setUserId(user.getId());
                userGroupRepository.save(userGroup);
            }
        }

        return user;
    }

    @ApiOperation(value = "删除用户信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/users/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUser(@PathVariable Integer id) {
        User user = getUserFromRequest(id);
        userRepository.delete(user);
        userRoleRepository.deleteByUserId(user.getId());
        userGroupRepository.deleteByUserId(user.getId());
    }




}
