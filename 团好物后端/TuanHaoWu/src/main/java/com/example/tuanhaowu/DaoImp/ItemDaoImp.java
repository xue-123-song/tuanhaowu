package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.ItemDao;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Orderitem;
import com.example.tuanhaowu.Repository.ItemRepository;
import com.example.tuanhaowu.Repository.OrderItemRepository;
import com.example.tuanhaowu.Repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ItemDaoImp implements ItemDao {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public Item saveOneItem(Item item)
    {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> searchGroupItem(Integer groupId) {
        return itemRepository.getAllByBelongGroupId(groupId);
    }

    @Override
    public Item getItemByItemId(Integer itemId) {
        return itemRepository.getItemByItemId(itemId);
    }

    @Override
    public void deleteItemByID(Integer itemId) { itemRepository.deleteById(itemId); }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemByUserID(String userID) {
        List<Order> orders =  orderRepository.searchAllByBelongUserid(userID);
        if (orders.size() == 0)
            return null;
       List<Integer> idList = new ArrayList<>();
       for (Order order:orders)
       {
           List<Orderitem> orderitems = orderItemRepository.getOrderitemsByBelongOrderId(order.getOrderId());
           for(Orderitem orderitem:orderitems)
           {
               idList.add(orderitem.getItemId());
           }
       }
       List<Item> items = new ArrayList<>();
        for (Integer integer : idList) {
            items.add(itemRepository.getItemByItemId(integer));
        }
       return items;
    }

    /**
     * @param groupId
     * @return 某个团购的所有itemId
     */
    @Override
    public List<Integer> getGroupItemId(int groupId) {
        return itemRepository.getItemIdByGroupId(groupId);
    }

    @Override
    public int getGroupIdByItemId(int itemId) {
        return itemRepository.getItemByItemId(itemId).getBelongGroupId();
    }

    @Override
    public void secKillProduct(int itemId) {
        Item item = itemRepository.getItemByItemId(itemId);
        int stock = item.getItemStock();
        stock--;
        item.setItemStock(stock);
        itemRepository.save(item);
    }
}
