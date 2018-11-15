package com.liansen.identity.oauth;

import com.liansen.identity.domain.User;

import java.util.Collections;

/**
 * Created by CDZ on 2018/11/10.
 */
public class CustomUserDetails  extends org.springframework.security.core.userdetails.User {
    private User user;

    public CustomUserDetails(User user) {
        super(user.getUserName(), user.getPassword(), true, true, true, true, Collections.EMPTY_SET);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
