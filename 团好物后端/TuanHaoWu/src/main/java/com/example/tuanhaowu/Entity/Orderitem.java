package com.example.tuanhaowu.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name ="orderitem")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","filedHandler"})
public class Orderitem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_item_index")
    private int orderItemIndex;
    @Basic
    @Column(name = "belong_order_id")
    private int belongOrderId;
    @Basic
    @Column(name = "item_buynum")
    private int itemBuynum;
    @Basic
    @Column(name = "item_id")
    private int itemId;
    @Basic
    @Column(name = "item_name")
    private String itemName;
    @Basic
    @Column(name = "item_image")
    private String itemImage;
    @Basic
    @Column(name = "item_description")
    private String itemDescription;
    @Basic
    @Column(name = "item_price")
    private int itemPrice;

    public int getOrderItemIndex() {
        return orderItemIndex;
    }

    public void setOrderItemIndex(int orderItemIndex) {
        this.orderItemIndex = orderItemIndex;
    }

    public int getBelongOrderId() {
        return belongOrderId;
    }

    public void setBelongOrderId(int belongOrderId) {
        this.belongOrderId = belongOrderId;
    }

    public int getItemBuynum() {
        return itemBuynum;
    }

    public void setItemBuynum(int itemBuynum) {
        this.itemBuynum = itemBuynum;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderitem orderitem = (Orderitem) o;
        return orderItemIndex == orderitem.orderItemIndex && belongOrderId == orderitem.belongOrderId && itemBuynum == orderitem.itemBuynum && itemId == orderitem.itemId && itemPrice == orderitem.itemPrice && Objects.equals(itemName, orderitem.itemName) && Objects.equals(itemImage, orderitem.itemImage) && Objects.equals(itemDescription, orderitem.itemDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemIndex, belongOrderId, itemBuynum, itemId, itemName, itemImage, itemDescription, itemPrice);
    }
}
