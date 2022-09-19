package com.example.tuanhaowu.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="items")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","filedHandler"})
public class Item {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id")
    private int itemId;
    @Basic
    @Column(name = "belong_group_id")
    private int belongGroupId;
    @Basic
    @Column(name = "item_name")
    private String itemName;
    @Basic
    @Column(name = "item_price")
    private int itemPrice;
    @Basic
    @Column(name = "item_image")
    private String itemImage;
    @Basic
    @Column(name = "item_description")
    private String itemDescription;
    @Basic
    @Column(name = "item_stock")
    private int itemStock;
    @Basic
    @Column(name = "item_seckill")
    private int itemSeckill;
    @Basic
    @Column(name = "sk_start_time")
    private Timestamp skStartTime;
    @Basic
    @Column(name = "sk_end_time")
    private Timestamp skEndTime;
    @Basic
    @Column(name = "image1")
    private  String image1;
    @Basic
    @Column(name = "image2")
    private  String image2;
    @Basic
    @Column(name = "number_buy")
    private  Integer number;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getBelongGroupId() {
        return belongGroupId;
    }

    public void setBelongGroupId(int belongGroupId) {
        this.belongGroupId = belongGroupId;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
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

    public int getItemStock() {
        return itemStock;
    }

    public void setItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    public int getItemSeckill() {
        return itemSeckill;
    }

    public void setItemSeckill(int itemSeckill) {
        this.itemSeckill = itemSeckill;
    }

    public Timestamp getSkStartTime() {
        return skStartTime;
    }

    public void setSkStartTime(Timestamp skStartTime) {
        this.skStartTime = skStartTime;
    }

    public Timestamp getSkEndTime() {
        return skEndTime;
    }

    public void setSkEndTime(Timestamp skEndTime) {
        this.skEndTime = skEndTime;
    }

    public Integer getNumber() { return number; }

    public void setNumber(Integer number) { this.number = number; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return number == item.number && itemId == item.itemId && belongGroupId == item.belongGroupId && itemPrice == item.itemPrice && itemStock == item.itemStock && itemSeckill == item.itemSeckill &&  Objects.equals(itemName, item.itemName) && Objects.equals(itemImage, item.itemImage) && Objects.equals(itemDescription, item.itemDescription) && Objects.equals(skStartTime, item.skStartTime) && Objects.equals(skEndTime, item.skEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, belongGroupId, itemName, itemPrice, itemImage, itemDescription, itemStock, itemSeckill, skStartTime, skEndTime,number);
    }
}
