package com.liansen.identity.response;

import com.liansen.common.model.ObjectMap;
//import com.liansen.identity.domain.Group;
import com.liansen.identity.domain.Group;
import com.liansen.identity.domain.Menu;
import com.liansen.identity.domain.Role;
import com.liansen.identity.domain.User;

import java.util.List;

/**
 * 数据返回转换工厂类
 *
 * @author cdy
 * @create 2018/9/5
 */
public class ConvertFactory {
    public static ObjectMap convertUseAuth(User user, String token) {
        return UserConverter.convertAuth(user, token);
    }

    public static List<ObjectMap> convertUserGroups(List<Group> groups, List<Group> roleGroups) {
        return GroupConverter.convertMultiSelect(groups, roleGroups);
    }

    public static List<ObjectMap> convertUseRoles(List<Role> roles, List<Role> userRoles) {
        return RoleConverter.convertMultiSelect(roles, userRoles);
    }

    public static List<ObjectMap> convertRoleMenus(List<Menu> menus, List<Menu> roleMenus) {
        return MenuConverter.convertMultiSelect(menus, roleMenus);
    }

    public static List<ObjectMap> convertUserMenus(List<Menu> parentMenus, List<Menu> childMenus) {
        return MenuConverter.convertUserMenus(parentMenus, childMenus);
    }
}
