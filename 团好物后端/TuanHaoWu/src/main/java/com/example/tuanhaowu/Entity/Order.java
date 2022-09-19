package com.example.tuanhaowu.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="orders")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","filedHandler"})
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private int orderId;
    @Basic
    @Column(name = "belong_userid")
    private String belongUserid;
    @Basic
    @Column(name = "group_id")
    private int groupId;
    @CreatedDate
    @Column(name = "order_finish_time")
    private Timestamp orderFinishTime;
    @Basic
    @Column(name = "tel")
    private String tel;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "order_status")
    private int orderStatus;
    @Basic
    @Column(name="receiver_name")
    private String receiverName;
    @Basic
    @Column(name="express_id")
    private String expressId;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    /*团员名称和头像*/
    @Transient //注释标记，防止在数据表中生成对应的字段
    private String name;
    @Transient
    private String picture;
    @Transient
    private String groupLeader;
    @Transient
    private String  LeaderPicture;

    public String getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(String groupLeader) {
        this.groupLeader = groupLeader;
    }

    public String getLeaderPicture() {
        return LeaderPicture;
    }

    public void setLeaderPicture(String leaderPicture) {
        LeaderPicture = leaderPicture;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }



    /**    update time：2022 7/26 （ps：绿色注释真好看！QAQ） by Xu
     *    订单状态， -4代表团长删除了订单，状态为已取消时删除订单
     *             -3代表团长删除了订单，状态已收货完成删除，与整数代表的状态相对应
     *             -13代表用户删除订单，状态为已收货时删除
     *             -14代表用户删除订单，状态为被取消时删除
     *             -103代表用户和团长在已取消状态下一起完成了删除
     *             -104代表用户和团长在已完成状态下一起完成了删除
     *             0代表未支付，
     *             1代表已支付未发货，
     *             2是已发货，
     *             3代表已收货，
     *             4代表被取消
     */

    @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.DETACH)
    @JoinColumn(name = "belong_order_id" , updatable = false)
    private List<Orderitem>  orderitemList;

    public Order(){}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getBelongUserid() {
        return belongUserid;
    }

    public void setBelongUserid(String belongUserid) {
        this.belongUserid = belongUserid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public Timestamp getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(Timestamp orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Orderitem> getOrderitemList() { return orderitemList; }

    public void setOrderitemList(List<Orderitem> orderitemList) { this.orderitemList = orderitemList; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId && belongUserid == order.belongUserid && groupId == order.groupId && orderStatus == order.orderStatus && Objects.equals(orderFinishTime, order.orderFinishTime) && Objects.equals(tel, order.tel) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, belongUserid, groupId, orderFinishTime, tel, address, orderStatus);
    }

}
