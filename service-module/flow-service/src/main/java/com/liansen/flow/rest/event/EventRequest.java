package com.liansen.flow.rest.event;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by CDZ on 2018/10/31.
 */
@Entity
@Table(name = "act_event", catalog = "liansen_flow")
@NamedQuery(name = "EventRequest.findAll", query = "SELECT d FROM EventRequest d")
public class EventRequest {
    private Integer id;
    private String eventName;
    private String remark;
    private String sql;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "event_name")
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "sql_name")
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
