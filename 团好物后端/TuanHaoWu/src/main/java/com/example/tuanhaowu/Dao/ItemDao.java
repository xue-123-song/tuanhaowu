package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Item;

import java.util.List;

public interface ItemDao {
    Item saveOneItem(Item item);

    List<Item> searchGroupItem(Integer groupId);

    List<Item> getAll();
    List<Item> getItemByUserID(String userID);
    Item getItemByItemId(Integer itemId);
    void deleteItemByID(Integer itemId);
    /**
     * @param groupId
     * @return 返回某个团购的所有的商品id
     * by Xu
     */
    List<Integer> getGroupItemId(int groupId);

    /**
     * 返回商品的groupId
     * by Xu
     */
    int getGroupIdByItemId(int itemId);


    void secKillProduct(int itemId);
}
