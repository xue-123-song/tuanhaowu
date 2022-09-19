package com.example.tuanhaowu.Controller;

import com.example.tuanhaowu.Service.HistoryService;
import com.example.tuanhaowu.Service.StatisticService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * 有关数据分析的控制类，可以写在这里哟!
 * created by Xu
 */
@RestController
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @Autowired
    HistoryService historyService;

    /**
     * 返回某个团购的所有商品的销售数量
     * @param groupId 传过来一个groupId
     * @return JSONArray
     * by Xu
     */
    @RequestMapping(value = "/statistic/itemSellNum/{groupId}")
    public JSONArray getItemSellNum(@PathVariable("groupId") int groupId)
    {
        return statisticService.getItemSellNum(groupId);
    }

    /**
     * 返回某个团购的浏览次数，在groupController中也添加了此数据的返回
     * @param groupId
     * @return int
     * by Xu
     */

    @RequestMapping(value = "/statistic/getVisitTimes/{groupId}")
    public int getVisitTimes(@PathVariable("groupId") int groupId)
    {
        return  historyService.getVisitTimes(groupId);
    }

    /**
     * 返回在今天新增的浏览次数
     * @param groupId
     * @return int
     * by Xu
     */
    @RequestMapping(value = "/statistic/todayIncrementVisit/{groupId}")
    public int todayIncrementVisit(@PathVariable("groupId") int groupId) throws ParseException {
        return historyService.todayIncrementVisit(groupId);
    }

}
