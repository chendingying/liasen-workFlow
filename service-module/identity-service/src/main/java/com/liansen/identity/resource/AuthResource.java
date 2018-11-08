package com.liansen.identity.resource;

import com.liansen.common.annotation.NotAuth;
import com.liansen.common.constant.CoreConstant;
import com.liansen.common.model.ObjectMap;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.utils.DateUtils;
import com.liansen.identity.constant.ErrorConstant;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.Menu;
import com.liansen.identity.domain.User;
import com.liansen.identity.domain.UserPassword;
import com.liansen.identity.repository.MenuRepository;
import com.liansen.identity.repository.UserRepository;
import com.liansen.identity.response.ConvertFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

/**
 * 授权控制类
 * @author cdy
 * @create 2018/9/5
 */
@Api(description = "授权控制")
@RestController
public  class AuthResource extends BaseResource {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MenuRepository menuRepository;


    /**
     * 登录系统
     * @param loginRequest
     * @return
     */
    @ApiIgnore
    @ApiOperation(value = "登录系统" , httpMethod = "POST")
    @PostMapping("/auths/login")
    @ResponseStatus(HttpStatus.OK)
    @NotAuth
    public ObjectMap login(@RequestBody ObjectMap loginRequest) {
        String account = loginRequest.getAsString("account");
        String password = loginRequest.getAsString("password");
        User user = userRepository.findByUserName(account);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }
        String userPassword = user.getPassword().replace("$2y","$2a");
        //密码密文匹配检测
        if (!BCrypt.checkpw(password, userPassword)){
            exceptionFactory.throwConflict(ErrorConstant.USER_PWD_NOT_MATCH);
        }
        String token = Jwts.builder().setSubject(account).setId(user.getId().toString()).setIssuedAt(DateUtils.currentTimestamp())
                .setExpiration(new Date(DateUtils.currentTimeMillis() + CoreConstant.LOGIN_USER_EXPIRE_IN)).signWith(SignatureAlgorithm.HS256, CoreConstant.JWT_SECRET).compact();
        return ConvertFactory.convertUseAuth(user, token);
    }

    @GetMapping(value = "/auths/token/{account}")
    @ResponseStatus(HttpStatus.OK)
    @NotAuth
    public ObjectMap token(@PathVariable(name = "account") String account){
        User user = userRepository.findByUserName(account);
        String token = Jwts.builder().setSubject(account).setId(user.getId().toString()).setIssuedAt(DateUtils.currentTimestamp())
                .setExpiration(new Date(DateUtils.currentTimeMillis() + CoreConstant.LOGIN_USER_EXPIRE_IN)).signWith(SignatureAlgorithm.HS256, CoreConstant.JWT_SECRET).compact();
        return ConvertFactory.convertUseAuth(user, token);
    }

    @ApiOperation(value = "根据用户Id查询权限菜单" , httpMethod = "GET")
    @GetMapping("/auths/menus")
    @ResponseStatus(HttpStatus.OK)
    public List<ObjectMap> getUserMenus(@RequestParam Integer userId) {
        List<Menu> childMenus = menuRepository.findByUserId(userId);
        List<Menu> parentMenus = menuRepository.findByTypeAndStatusOrderByOrderAsc(TableConstant.MENU_TYPE_PARENT, TableConstant.MENU_STATUS_NORMAL);
        return ConvertFactory.convertUserMenus(parentMenus, childMenus);
    }

    /**
     * 修改密码
     * @param changeRequest
     * @return
     */
    @ApiOperation(value = "修改密码" , httpMethod = "PUT")
    @PutMapping("/auths/password/change")
    @ResponseStatus(HttpStatus.OK)
    public User changePwd(@RequestBody UserPassword changeRequest) {
        String newPassword = changeRequest.getNewPassword();
        String confirmPassword = changeRequest.getConfirmPassword();
        String oldPassword = changeRequest.getOldPassword();
        Integer userId = changeRequest.getUserId();
        if (!newPassword.equals(confirmPassword)) {
            exceptionFactory.throwConflict(ErrorConstant.USER_PASSWORD_CONFIRM_ERROR);
        }

        User user = userRepository.findOne(userId);
        if (user == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.USER_NOT_FOUND);
        }

        if (!user.getPassword().equals(oldPassword)) {
            exceptionFactory.throwConflict(ErrorConstant.USER_OLD_PASSWORD_WRONG);
        }

        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public static void main(String args[]){

        String candidate="$2a$10$zCNqWeBPfOzH95flLveBlOG5E.aeiT1T98ES9HW8ZCcimcwdFDKA6";
        String password="admin";
        //密码密文匹配检测
        if (BCrypt.checkpw(password, candidate))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }
}
