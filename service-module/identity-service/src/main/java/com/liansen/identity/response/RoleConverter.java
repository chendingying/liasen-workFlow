package com.liansen.identity.response;

import com.liansen.common.model.ObjectMap;
import com.liansen.common.utils.ObjectUtils;
import com.liansen.identity.domain.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色数据转换表
 * @author cdy
 * @create 2018/9/5
 */
public class RoleConverter {
    public static List<ObjectMap> convertMultiSelect(List<Role> roles, List<Role> userRoles) {
        List<ObjectMap> menuList = new ArrayList<ObjectMap>();
        for (Role role : roles) {
            if (ObjectUtils.isNotEmpty(userRoles) && userRoles.contains(role)) {
                menuList.add(ObjectMap.of("id", role.getId(), "name", role.getName(), "selected", true));
            } else {
                menuList.add(ObjectMap.of("id", role.getId(), "name", role.getName(), "selected", false));
            }
        }
        return menuList;
    }
}
