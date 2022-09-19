package com.example.tuanhaowu.Entity;

import net.bytebuddy.agent.builder.AgentBuilder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "group_buy", schema = "thwapp")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","filedHandler"})
public class Group {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "group_id")
    private int groupId;
    @Basic
    @Column(name = "group_leader")
    private String groupLeader;
    @Basic
    @Column(name = "group_title")
    private String groupTitle;
    @Basic
    @Column(name = "group_description")
    private String groupDescription;
    @Basic
    @Column(name = "logistics")
    private int logistics;
    @Basic
    @Column(name = "start_time")
    private Timestamp startTime;
    @Basic
    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     *  1表示团购正在进行中，0表示团购已经结束了，-1表示团购被删除了，就不传到前端去显示
     *  然后设置删除状态的时候，需要所有的订单都必须已经结束了，处于3，4，或者负数的状态
     */
    @Basic
    @Column(name = "group_status")
    private  Integer status;
    @Transient
    private String picture;
    @Transient
    private Integer subscribeId;

    public Integer getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(Integer subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.REMOVE)
    @JoinColumn(name = "belong_group_id",updatable = false)
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.DETACH)
    @JoinColumn(name = "group_id",updatable = false)
    private  List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(String groupLeader) {
        this.groupLeader = groupLeader;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public int getLogistics() {
        return logistics;
    }

    public void setLogistics(int logistics) {
        this.logistics = logistics;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return groupId == group.groupId && logistics == group.logistics && Objects.equals(groupLeader, group.groupLeader) && Objects.equals(groupTitle, group.groupTitle) && Objects.equals(groupDescription, group.groupDescription) && Objects.equals(startTime, group.startTime) && Objects.equals(endTime, group.endTime) && Objects.equals(status, group.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupLeader, groupTitle, groupDescription, logistics, startTime, endTime, status);
    }
}
