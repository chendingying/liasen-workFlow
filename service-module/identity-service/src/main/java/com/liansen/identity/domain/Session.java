package com.liansen.identity.domain;

/**
 * Created by CDZ on 2018/11/9.
 */
public class Session {
    private String laravel_session;
    private String token;

    public String getLaravel_session() {
        return laravel_session;
    }

    public void setLaravel_session(String laravel_session) {
        this.laravel_session = laravel_session;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
