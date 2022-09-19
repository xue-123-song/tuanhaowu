package com.example.tuanhaowu.Controller;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Constant.constant;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Entity.Order;
import com.example.tuanhaowu.Entity.Request.GroupRequestReceiver;
import com.example.tuanhaowu.Entity.Response.SuccessResponse;
import com.example.tuanhaowu.Service.*;
import com.example.tuanhaowu.util.AuthUtil;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgCode;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import com.example.tuanhaowu.util.TimeUtil;
import javafx.util.Pair;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/* QAQ 此页面应该没有Dao层的东西了，重构经过postman测试无误 by Xu*/
@CrossOrigin
@RestController
public class GroupController {
    @Autowired
    ItemService itemService;

    @Autowired
    GroupService groupService;
    @Autowired
    OrderService orderService;
    @Autowired
    AuthService authService;
    HistoryService historyService;

    @Autowired
    PushService pushService;

    /*时间转化函数
    * 接受：前端的时间string
    * 返回；后端时间戳类型
    * 已经提取成一个方法类了，建Util/timeutil
    * by Xu
    * */

    /**创建团购
    * 接受：所有的团购信息
    * 返回：团购的所有信息
    * 7/8 重新分层
     * by Xu
    * */
    @RequestMapping("/createGroup")
    public  Msg creat(@RequestBody JSONObject GroupInfo) throws SchedulerException {
        System.out.println(GroupInfo);
        /*解析团购名称*/
        String groupTitle=GroupInfo.getString("groupTitle");
        String groupLeader=GroupInfo.getString("groupLeader");
        String description;
        try {
            description = GroupInfo.getString("groupDescription");
        } catch (Exception e) {
            description = null;
        }
        Integer logistics = GroupInfo.getInt("logistics");
        JSONArray items =GroupInfo.optJSONArray("items");
        String startTime = GroupInfo.getString("startTime");
        Timestamp sTime = TimeUtil.timeTrim(startTime);
        String endTime = GroupInfo.getString("endTime");
        Timestamp eTime = TimeUtil.timeTrim(endTime);
        Group newGroup = groupService.CreateGroup(groupLeader,groupTitle,description,logistics,sTime,eTime);
        int belongtoGroupID = newGroup.getGroupId() ;
        System.out.println("newGroup's id = "+belongtoGroupID);
        System.out.println(items);
        /*测试item数组的长度*/
        System.out.println(items.size());
        List<Item> itemList=new ArrayList<>();
        for (Object o : items) {
            JSONObject item = (JSONObject) o;
            System.out.println(item);
            String itemName = item.getString("itemName");
            int itemPrice = item.getInt("itemPrice");
            int itemStock = item.getInt("itemStock");
            boolean itemSeckill = item.getBoolean("itemSeckill");
            int flag = 0;
            if (itemSeckill) flag = 1;
            System.out.println("itemskill = " + itemSeckill);
            String itemDescription;
            try {
                itemDescription = item.getString("itemDescription");
            } catch (Exception e) {
                itemDescription = null;
            }
            String [] url=new String[constant.IMAGE_NUM];
            for (int i = 0; i < constant.IMAGE_NUM ; ++i) {
                try {
                    JSONArray itemImage = item.optJSONArray("itemImage");
                    JSONObject urlJSON = (JSONObject) itemImage.get(i);
                    url[i] = urlJSON.getString("url");
                } catch (Exception e) {
                    url[i] = null;
                }
            }
            //System.out.println(url.getString("url"));
            Timestamp secKillStartTime = null, secKillEndTime = null;
            if (flag == 1) {

                secKillStartTime = TimeUtil.timeTrim(item.getString("skStartTime"));
                secKillEndTime = TimeUtil.timeTrim(item.getString("skEndTime"));
            }
            Item tmp = itemService.InsertOneItem(itemName,
                    belongtoGroupID,
                    itemPrice,
                    url,
                    itemStock,
                    flag,
                    itemDescription,
                    secKillStartTime,
                    secKillEndTime);
            itemList.add(tmp);
        }
        newGroup.setItems(itemList);
        Group group = groupService.saveOneGroup(newGroup);
        return MsgUtil.makeMsg(MsgCode.CREATE_GROUP_SUCCESS);
    }
/*这里修改了传参方式*/
    @RequestMapping("/searchGroupByGroupLeader/{GroupLeader}")
    public List<Group> searchGroupByGroupLeader(@PathVariable("GroupLeader") String GroupLeader)
    {
        return groupService.searchGroupByGroupLeader(GroupLeader);
    }

    @RequestMapping("/searchGroupItemByGroupID/{groupId}")
    public List<Item> searchGroupItem(@PathVariable("groupId") Integer groupId)
    {
        return itemService.searchGroupItem(groupId);
    }

    @RequestMapping("/searchAllGroup")
    public List<Group> searchAllGroup()
    {
        return groupService.searchAllGroup();
    }
    /**
    * 修改团购信息
    * 接受：除了item之外的信息
    * 返回：被修改信息之后的group
    * by Xu 测试无误
    * 7/8 重新分层
    * 7/11 修改了返回值，返回Msg和所有的团购信息，返回的例子 如下：
    * 马哥看看能不能满足要求
    * {
	"key": {
		"status": 0,
		"msg": "修改团购信息成功！",
		"data": null
	},
	"value": [
		{group1的信息}
		{group2的信息}
		....
		 }]
	}
    *
    * */

    @RequestMapping("/changeGroupInfo")
    public Pair<Msg,List<Group>> changeGroupInfo (@RequestBody GroupRequestReceiver GroupInfo) {
        System.out.println(GroupInfo.getGroupId());
        Integer groupID = GroupInfo.getGroupId();
        if (!authService.isGroupLeader(AuthUtil.getAuthUserId(),groupID)) return null;//如果不是该团购的团长，无权修改团购信息
        String  groupLeader = GroupInfo.getGroupLeader();
        String  groupTitle = GroupInfo.getGroupTitle();
        String   groupDescription = GroupInfo.getGroupDescription();
        Integer logistics = GroupInfo.getLogistics();
       Timestamp startTime = TimeUtil.timeTrim(GroupInfo.getStartTime());
       Timestamp endTime = TimeUtil.timeTrim(GroupInfo.getEndTime());

       Group gp = groupService.getGroupByID(groupID);
        List<Group> allGroup = null;
       try {
           gp.setGroupTitle(groupTitle);
           gp.setGroupLeader(groupLeader);
           gp.setEndTime(endTime);
           gp.setLogistics(logistics);
           gp.setStartTime(startTime);
           gp.setGroupDescription(groupDescription);
           groupService.saveOneGroup(gp);
           allGroup = groupService.searchGroupByGroupLeader(groupLeader);
       }catch (Error error)
       {
           return new Pair<>(MsgUtil.makeMsg(MsgCode.CHANGE_ERROR), null);
       }
       pushService.RenewPush(groupID);
       return new Pair<>(MsgUtil.makeMsg(MsgCode.CHANGE_SUCCESS),allGroup);
    }

    /**
     * update:2022 7/26 by Xu
    * 删除团购
    * 前端传的是：groupId
    * 后端完成删除，目前是设置团购的状态为-1认定为删除
    * */
    @RequestMapping("/deleteGroupByID/{groupID}")
    public Msg deleteGroupByID(@PathVariable("groupID") Integer groupID)
    {
        if (!authService.isGroupLeader(AuthUtil.getAuthUserId(),groupID)) return MsgUtil.makeMsg(MsgUtil.ERROR,"您没有权限删除不属于自己的团购");
        int cond =groupService.deleteGroupByID(groupID);
        switch (cond){
            case 0: return MsgUtil.makeMsg(MsgUtil.SUCCESS,"删除团购成功");
            case -1: return MsgUtil.makeMsg(MsgUtil.ERROR,"当前团购有尚未处理的订单，请先处理订单再删除");
            case -2:return MsgUtil.makeMsg(MsgUtil.ERROR,"团购删除出错啦，快联系程序员处理吧！！");
            default:
                throw new IllegalStateException("Unexpected value: " + cond);
        }
    }

    @RequestMapping("/tourist/searchGroupByGroupId/{groupId}")
    public Group searchGroupByGroupId(@PathVariable("groupId") Integer groupId)
    {
        return groupService.searchGroupByGroupId(groupId);
    }

    /*
     *团长取消团购
     *团长传回groupid
     *团购信息group及item被删除，用户收到退款，修改订单状态
     * by Zhou
     */
    @RequestMapping("/cancelGroup/{groupId}")
    public SuccessResponse cancelGroup(@PathVariable("groupId") Integer groupId)
    {
        return groupService.cancelGroup(groupId);
    }
    /**
     * update:2022 7/26 错误时直接不指定数据为-1，增加了退款总额的计算
    * 数据统计分析：
    * 查询订单数量
    * 返回值类似于：
   {
	"status": 0,
	"msg": "查找数据成功",
	"data": {
		"empty": true,
		"orderNum": 3,
		"totalSales": 72468,
		"participants": 2
	}
}
* 进行了一定的异常处理，虽然可能用不上，就是groupId不存在的时候，会返回错误
* By Xu
    * */

    @RequestMapping("/statistic/QueryData/{groupId}")
    public Msg QueryOrderNum(@PathVariable("groupId") Integer groupId)
    {

        if (!authService.isGroupLeader(AuthUtil.getAuthUserId(),groupId)) return MsgUtil.makeMsg(MsgUtil.ERROR,"您没有权限查询不属于自己的团购");
        int  participants = 0;;
        int[] sales = new int[0];
        int[] orderNum = new int[0];
        net.sf.json.JSONObject data = net.sf.json.JSONObject.fromObject(new JSONObject());
        try {
            Group group = groupService.getGroupByID(groupId);
            System.out.println("group"+JSON.toJSONString(group));
        List<Order> orders = group.getOrders();

           orderNum = groupService.getOrderNum(orders);
           sales = groupService.getGroupSales(orders);
          //  participants = groupService.getGroupParticipants(orders);

           data.put("ValidOrderNum",orderNum[0]);  //成交的订单数量
           data.put("cancelOrderNum",orderNum[1]); //取消了的订单数量
          data.put("totalSales",sales[0]);        //团购销售的金额
            data.put("withdraw",sales[1]);          //团购退款金额
          // data.put("participants",participants);  //团购参与人数
       }catch (Exception err)
       {

            data.put("ValidOrderNum",orderNum[0]);
            data.put("cancelOrderNum",orderNum[1]);
            data.put("totalSales",sales[0]);
            data.put("withdraw",sales[1]);          //退款
            data.put("participants",participants);
            data.put("visitTimes",historyService.getVisitTimes(groupId));
        }

        return MsgUtil.makeMsg(MsgUtil.SUCCESS,"查找数据成功", data);
    }

    /**
    * 团购的模糊查询
    * 可传入团长姓名字段，团购名称和商品的姓名字段，查询对应的团购
     *  9/8 更新  -------------------> 增加路径前缀
     *  *  by Xu
    * */
    @RequestMapping("/tourist/FuzzyQueryByGroupLeader/{groupLeader}")
    public List<Group> FuzzyQueryByGroupLeader(@PathVariable("groupLeader") String groupLeader)
    {
        return groupService.FuzzyQueryByGroupLeader(groupLeader);
    }
    @RequestMapping("/tourist/FuzzyQueryByGroupTitle/{groupTitle}")
    public List<Group> FuzzyQueryByGroupTitle(@PathVariable("groupTitle") String groupTitle)
    {
        return groupService.FuzzyQueryByGroupTitle(groupTitle);
    }

    @RequestMapping("/tourist/FuzzyQueryByItemName/{itemName}")
    public List<Group> FuzzyQueryByItemName(@PathVariable("itemName") String itemName)
    {
        return groupService.FuzzyQueryByItemName(itemName);
    }
    
//controller的末尾！
}

