package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.ItemDao;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ItemServiceImp implements ItemService {
    @Autowired
    ItemDao itemDao;

    /**
     *Item的增加
     * by Xu
     */
    @Override
    public Item InsertOneItem(String itemName, int belongGroupId, int itemPrice, String [] itemImage, int itemStock, int itemSeckill, String itemDescription, Timestamp secKillStartTime, Timestamp secKillEndTime) {
        Item item = new Item();
        item.setBelongGroupId(belongGroupId);
        item.setNumber(0);
        setItemBasicInfo(itemName, itemPrice, itemImage, itemStock, itemSeckill, itemDescription, secKillStartTime, secKillEndTime, item);
        return itemDao.saveOneItem(item);
    }
    /**
     *Item的修改
     * by Xy
     */
    @Override
    public Item modifyItemInfo(int itemId, String itemName, int itemPrice, String[] itemImage, int itemStock, int flag, String itemDescription, Timestamp secKillStartTime, Timestamp secKillEndTime) {
        Item item = itemDao.getItemByItemId(itemId);
        setItemBasicInfo(itemName, itemPrice, itemImage, itemStock, flag, itemDescription, secKillStartTime, secKillEndTime, item);
        return itemDao.saveOneItem(item);
    }

    /**
     * 设置item基本信息的工具函数
     * by Xu
     */
    private void setItemBasicInfo(String itemName, int itemPrice, String[] itemImage, int itemStock, int flag, String itemDescription, Timestamp secKillStartTime, Timestamp secKillEndTime, Item item) {
        item.setItemName(itemName);
        item.setItemPrice(itemPrice);
        item.setItemImage(itemImage[0]);
        item.setImage1(itemImage[1]);
        item.setImage2(itemImage[2]);
        item.setItemStock(itemStock);
        item.setItemSeckill(flag);
        item.setItemDescription(itemDescription);
        item.setSkStartTime(secKillStartTime);
        item.setSkEndTime(secKillEndTime);
    }

    /**
     *Item的查找
     * by Xu
     */
    @Override
    public List<Item> searchGroupItem(Integer groupId) {

        return itemDao.searchGroupItem(groupId);
    }
    /**
     *Item的删除
     * by Xu
     */
    @Override
    public void deleteItemByID(Integer itemID) {
       itemDao.deleteItemByID(itemID);
    }
}
