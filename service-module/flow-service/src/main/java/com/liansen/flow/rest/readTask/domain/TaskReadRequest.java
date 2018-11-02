package com.liansen.flow.rest.readTask.domain;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by CDZ on 2018/10/31.
 */
@Entity
@Table(name = "act_read_task", catalog = "liansen_flow")
@NamedQuery(name = "TaskReadRequest.findAll", query = "SELECT d FROM TaskReadRequest d")
public class TaskReadRequest {
    private Integer Id;
    private String processDefinitionId;
    private String userId;
    private String status;
    private String url;
    private String taskId;

    @javax.persistence.Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @Column(name = "process_definition_id")
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
