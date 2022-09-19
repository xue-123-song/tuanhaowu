package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.ItemDao;
import com.example.tuanhaowu.Dao.OrderItemDao;
import com.example.tuanhaowu.Service.StatisticService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImp implements StatisticService {
    @Autowired
    ItemDao itemDao;
    @Autowired
    OrderItemDao orderItemDao;

    /**
     * @param groupId
     * @return 返回某个团购所有商品的销售数量，返回商品名称和销售数量
     * by Xu
     */
    @Override
    public JSONArray getItemSellNum(int groupId) {
        List<Integer> itemIdList = itemDao.getGroupItemId(groupId);
        JSONArray orderitem;
        JSONArray result = new JSONArray();
        List<Object> res = new ArrayList<>();
        for (int i:itemIdList)
        {
            orderitem=orderItemDao.getOrderItemSellNum(i);
            if (orderItemDao.isItemInOrderItem(i))
            {
                result.addAll(orderitem);
            }
            else {
                String itemName = itemDao.getItemByItemId(i).getItemName();
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(itemName);
                jsonArray.add(0);
                result.add(jsonArray);
            }
        }
        return result;
    }



}
