package com.example.tuanhaowu.Service;

import net.sf.json.JSONArray;


public interface StatisticService {
    /**
     * @param groupId
     * @return 返回某个团购所有商品的销售数量，返回商品名称和销售数量
     * by Xu
     */
    JSONArray getItemSellNum(int groupId);
}
