package com.liansen.identity.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by CDZ on 2018/10/15.
 */
public class UserPassword {

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("确认密码")
    private String confirmPassword;

    @ApiModelProperty("旧密码")
    private String oldPassword;

    @ApiModelProperty("用户Id")
    private Integer userId;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
