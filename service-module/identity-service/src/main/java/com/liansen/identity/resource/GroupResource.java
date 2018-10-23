package com.liansen.identity.resource;


import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.identity.constant.ErrorConstant;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.Group;
import com.liansen.identity.domain.User;
import com.liansen.identity.repository.GroupRepository;
import com.liansen.identity.repository.UserGroupRepository;
import com.liansen.identity.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 群组资源控制类
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "群组接口")
@RestController
public class GroupResource extends BaseResource {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    private Group getGroupFromRequest(Integer id) {
        Group group = groupRepository.findOne(id);
        if (group == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.GROUP_NOT_FOUND);
        }
        return group;
    }

    @ApiOperation(value = "群组查询" , httpMethod = "GET")
    @GetMapping(value = "/groups")
    @ResponseStatus(value = HttpStatus.OK)
    public PageResponse getGroups(@ApiIgnore @RequestParam Map<String, String> requestParams) {
        Criteria<Group> criteria = new Criteria<Group>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.eq("type", requestParams.get("type")));
        criteria.add(Restrictions.eq("status", requestParams.get("status")));
        criteria.add(Restrictions.eq("parentId", requestParams.get("parentId")));
        criteria.add(Restrictions.like("name", requestParams.get("name")));
        criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId")));
        return createPageResponse(groupRepository.findAll(criteria, getPageable(requestParams)));
    }

    @ApiOperation(value = "根据群组Id查询" , httpMethod = "GET")
    @GetMapping(value = "/groups/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Group getGroup(@PathVariable Integer id) {
        return getGroupFromRequest(id);
    }

    @ApiOperation(value = "添加群组信息" , httpMethod = "POST")
    @PostMapping("/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody Group groupRequest) {
        Group group = new Group();
        group.setType(groupRequest.getType());
        group.setParentId(groupRequest.getParentId());
        group.setOrder(groupRequest.getOrder());
        group.setName(groupRequest.getName());
        group.setRemark(groupRequest.getRemark());
        group.setStatus(groupRequest.getStatus());
        return groupRepository.save(group);
    }

    @ApiOperation(value = "修改群组信息" , httpMethod = "PUT")
    @PutMapping(value = "/groups/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Group updateGroup(@PathVariable Integer id, @RequestBody Group groupRequest) {
        Group group = getGroupFromRequest(id);
        group.setName(groupRequest.getName());
        group.setStatus(groupRequest.getStatus());
        group.setType(groupRequest.getType());
        group.setOrder(groupRequest.getOrder());
        group.setParentId(groupRequest.getParentId());
        group.setRemark(groupRequest.getRemark());
        return groupRepository.save(group);
    }

    @ApiOperation(value = "修改群组状态" , httpMethod = "PUT")
    @PutMapping(value = "/groups/{id}/switch")
    @ResponseStatus(value = HttpStatus.OK)
    public Group switchStatus(@PathVariable Integer id) {
        Group group = getGroupFromRequest(id);
        if (group.getStatus() == TableConstant.GROUP_STATUS_NORMAL) {
            group.setStatus(TableConstant.GROUP_STATUS_STOP);
        } else {
            group.setStatus(TableConstant.GROUP_STATUS_NORMAL);
        }
        return groupRepository.save(group);
    }

    @ApiOperation(value = "群组下的用户信息" , httpMethod = "GET")
    @GetMapping(value = "/groups/{id}/users")
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getGroupUsers(@PathVariable Integer id) {
        return userRepository.findByGroupId(id);
    }

    @ApiOperation(value = "群组下的用户Id" , httpMethod = "GET")
    @GetMapping(value = "/groups/{id}/usersId")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Integer> getGroupUsersId(@PathVariable Integer id){return userRepository.findIdByGroupId(id);}
    @ApiOperation(value = "删除群组下的用户信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/groups/{id}/users/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteGroupUser(@PathVariable Integer id, @PathVariable(value = "userId") Integer userId) {
        Group group = getGroupFromRequest(id);
        userGroupRepository.deleteByGroupIdAndUserId(id, userId);
    }

    @ApiOperation(value = "删除群组信息" , httpMethod = "DELETE")
    @DeleteMapping(value = "/groups/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable Integer id) {
        Group group = getGroupFromRequest(id);
        if (group.getType() == TableConstant.GROUP_TYPE_PARENT) {
            List<Group> children = groupRepository.findByParentId(group.getId());
            if (ObjectUtils.isNotEmpty(children)) {
                exceptionFactory.throwForbidden(ErrorConstant.GROUP_HAVE_CHILDREN);
            }
        } else {
            List<User> users = userRepository.findByGroupId(group.getId());
            if (ObjectUtils.isNotEmpty(users)) {
                exceptionFactory.throwForbidden(ErrorConstant.Group_ALREADY_USER_USE, users.get(0).getName());
            }
        }
        groupRepository.delete(group);
    }
}
