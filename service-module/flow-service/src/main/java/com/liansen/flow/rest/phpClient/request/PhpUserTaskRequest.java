package com.liansen.flow.rest.phpClient.request;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by CDZ on 2018/10/22.
 */
@Entity
@Table(name = "act_hi_php_task", catalog = "liansen_flow")
@NamedQuery(name = "PhpUserTaskRequest.findAll", query = "SELECT d FROM PhpUserTaskRequest d")
public class PhpUserTaskRequest {
    private Integer id;
    private String taskId;
    private String phpTaskId;
    private String userId;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "php_task_id")
    public String getPhpTaskId() {
        return phpTaskId;
    }

    public void setPhpTaskId(String phpTaskId) {
        this.phpTaskId = phpTaskId;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
