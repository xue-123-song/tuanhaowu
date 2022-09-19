package com.example.tuanhaowu.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Subscribe {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "subscribe_id")
    private int subscribeId;
    @Basic
    @Column(name = "subscribe_user_id")
    private String subscribeUserId;
    @Basic
    @Column(name = "group_id")
    private int groupId;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

    public int getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getSubscribeUserId() {
        return subscribeUserId;
    }

    public void setSubscribeUserId(String subscribeUserId) {
        this.subscribeUserId = subscribeUserId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscribe subscribe = (Subscribe) o;
        return subscribeId == subscribe.subscribeId && groupId == subscribe.groupId && Objects.equals(subscribeUserId, subscribe.subscribeUserId) && Objects.equals(createTime, subscribe.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscribeId, subscribeUserId, groupId, createTime);
    }
}
