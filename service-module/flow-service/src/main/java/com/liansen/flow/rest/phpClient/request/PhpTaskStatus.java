package com.liansen.flow.rest.phpClient.request;

/**
 * Created by CDZ on 2018/10/29.
 */
public class PhpTaskStatus {
    private Boolean finish;
    private String id;

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
