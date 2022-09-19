package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Item;

import java.sql.Timestamp;
import java.util.List;

public interface ItemService {
    Item InsertOneItem(
            String itemName,
            int belongGroupId,
            int itemPrice,
            String [] itemImage,
            int itemStock,
            int itemSeckill,
            String itemDescription,
            Timestamp secKillStartTime,
            Timestamp secKillEndTime);

    Item modifyItemInfo(
            int itemId,
            String itemName,
            int itemPrice,
            String [] itemImage,
            int itemStock,
            int flag,
            String itemDescription,
            Timestamp secKillStartTime,
            Timestamp secKillEndTime);
    List<Item> searchGroupItem(Integer groupId);
    void deleteItemByID(Integer itemID);
}
