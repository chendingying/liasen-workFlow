package com.liansen.flow.rest.task.domain;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by CDZ on 2018/11/9.
 */
@Entity
@Table(name = "ACT_HI_LOGGER", catalog = "liansen_flow")
@NamedQuery(name = "HistoricLogger.findAll", query = "SELECT d FROM HistoricLogger d")
public class HistoricLogger {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID_", unique = true, nullable = false)
    private Integer id;

    @Column(name = "PROC_DEF_ID_")
    private String procDefId;

    @Column(name = "TASK_ID_")
    private String taskId;

    @Column(name = "TASK_NAME_")
    private String taskName;

    @Column(name = "USER_ID_")
    private String userId;


    @Column(name = "REMARK_")
    private String remark;

    @Column(name = "TIME_")
    private Timestamp time;

    @Column(name = "STATUS_")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
