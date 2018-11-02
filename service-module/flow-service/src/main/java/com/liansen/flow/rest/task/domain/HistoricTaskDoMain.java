package com.liansen.flow.rest.task.domain;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by CDZ on 2018/10/31.
 */
@Entity
@Table(name = "ACT_HI_TASKINST", catalog = "liansen_flow")
@NamedQuery(name = "HistoricTaskDoMain.findAll", query = "SELECT d FROM HistoricTaskDoMain d")
public class HistoricTaskDoMain {
    private String id;
    private Integer REV;
    private String processDefinitionId;
    private String taskDefinitionId;
    private String taskDefinitionKey;
    private String  processInstanceId;
    private String executionId;
    private String SCOPE_ID_;
    private String SUB_SCOPE_ID_;
    private String SCOPE_TYPE_;
    private String SCOPE_DEFINITION_ID_;
    private String name;
    private String parentTaskId;
    private String description;
    private String owner;
    private String assignee;
    private Date startTime;
    private Date claimTime;
    private Date endTime;
    private Boolean durationInMillis;
    private String DELETE_REASON_;
    private Integer priority;
    private Date dueDate;
    private String formKey;
    private String category;
    private String tenantId;
    private Date LAST_UPDATED_TIME_;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID_", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "REV_")
    public Integer getREV() {
        return REV;
    }

    public void setREV(Integer REV) {
        this.REV = REV;
    }

    @Column(name = "PROC_DEF_ID_")
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @Column(name = "TASK_DEF_ID_")
    public String gettaskDefinitionId() {
        return taskDefinitionId;
    }

    public void settaskDefinitionId(String taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    @Column(name = "TASK_DEF_KEY_")
    public String gettaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void settaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Column(name = "PROC_INST_ID_")
    public String getprocessInstanceId() {
        return processInstanceId;
    }

    public void setprocessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Column(name = "EXECUTION_ID_")
    public String getexecutionId() {
        return executionId;
    }

    public void setexecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Column(name = "SCOPE_ID_")
    public String getSCOPE_ID_() {
        return SCOPE_ID_;
    }

    public void setSCOPE_ID_(String SCOPE_ID_) {
        this.SCOPE_ID_ = SCOPE_ID_;
    }

    @Column(name = "SUB_SCOPE_ID_")
    public String getSUB_SCOPE_ID_() {
        return SUB_SCOPE_ID_;
    }

    public void setSUB_SCOPE_ID_(String SUB_SCOPE_ID_) {
        this.SUB_SCOPE_ID_ = SUB_SCOPE_ID_;
    }

    @Column(name = "SCOPE_TYPE_")
    public String getSCOPE_TYPE_() {
        return SCOPE_TYPE_;
    }

    public void setSCOPE_TYPE_(String SCOPE_TYPE_) {
        this.SCOPE_TYPE_ = SCOPE_TYPE_;
    }

    @Column(name = "SCOPE_DEFINITION_ID_")
    public String getSCOPE_DEFINITION_ID_() {
        return SCOPE_DEFINITION_ID_;
    }

    public void setSCOPE_DEFINITION_ID_(String SCOPE_DEFINITION_ID_) {
        this.SCOPE_DEFINITION_ID_ = SCOPE_DEFINITION_ID_;
    }

    @Column(name = "NAME_")
    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    @Column(name = "PARENT_TASK_ID_")
    public String getparentTaskId() {
        return parentTaskId;
    }

    public void setparentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Column(name = "DESCRIPTION_")
    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    @Column(name = "OWNER_")
    public String getowner() {
        return owner;
    }

    public void setowner(String owner) {
        this.owner = owner;
    }

    @Column(name = "ASSIGNEE_")
    public String getassignee() {
        return assignee;
    }

    public void setassignee(String assignee) {
        this.assignee = assignee;
    }

    @Column(name = "START_TIME_")
    public Date getstartTime() {
        return startTime;
    }

    public void setstartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "CLAIM_TIME_")
    public Date getclaimTime() {
        return claimTime;
    }

    public void setclaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    @Column(name = "END_TIME_")
    public Date getendTime() {
        return endTime;
    }

    public void setendTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "DURATION_")
    public Boolean getdurationInMillis() {
        return durationInMillis;
    }

    public void setdurationInMillis(Boolean durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    @Column(name = "DELETE_REASON_")
    public String getDELETE_REASON_() {
        return DELETE_REASON_;
    }

    public void setDELETE_REASON_(String DELETE_REASON_) {
        this.DELETE_REASON_ = DELETE_REASON_;
    }

    @Column(name = "PRIORITY_")
    public Integer getpriority() {
        return priority;
    }

    public void setpriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "DUE_DATE_")
    public Date getdueDate() {
        return dueDate;
    }

    public void setdueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Column(name = "FORM_KEY_")
    public String getformKey() {
        return formKey;
    }

    public void setformKey(String formKey) {
        this.formKey = formKey;
    }

    @Column(name = "CATEGORY_")
    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    @Column(name = "TENANT_ID_")
    public String gettenantId() {
        return tenantId;
    }

    public void settenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "LAST_UPDATED_TIME_")
    public Date getLAST_UPDATED_TIME_() {
        return LAST_UPDATED_TIME_;
    }

    public void setLAST_UPDATED_TIME_(Date LAST_UPDATED_TIME_) {
        this.LAST_UPDATED_TIME_ = LAST_UPDATED_TIME_;
    }


}
