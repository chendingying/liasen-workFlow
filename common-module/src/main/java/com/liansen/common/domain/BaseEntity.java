package com.liansen.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author cdy
 * @create 2018/9/4
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class BaseEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户Id")
    protected Integer id;

    @ApiModelProperty("乐观锁版本号")
    protected Integer rev;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    protected Timestamp createTime;

    @JsonIgnore
    @ApiModelProperty("修改时间")
    protected Timestamp lastUpdateTime;

    @JsonIgnore
    @ApiModelProperty("租户ID")
    protected String tenantId;

    @PrePersist
    public void prePersist() {
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.lastUpdateTime = this.createTime;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateTime = new Timestamp(System.currentTimeMillis());
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID_", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Version
    @Column(name = "REV_")
    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    @Column(name = "CREATE_TIME_", length = 19)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Column(name = "LAST_UPDATE_TIME_", length = 19)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Column(name = "TENANT_ID_")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

}