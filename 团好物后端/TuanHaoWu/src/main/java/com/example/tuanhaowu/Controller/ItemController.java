package com.example.tuanhaowu.Controller;

import com.example.tuanhaowu.Constant.constant;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Service.AuthService;
import com.example.tuanhaowu.Service.ItemService;
import com.example.tuanhaowu.util.AuthUtil;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import com.example.tuanhaowu.util.TimeUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
public class ItemController {

    @Autowired
    ItemService  itemService;
    @Autowired
    AuthService authService;
    /**
    * 完成团购商品的添加
    * 传入团购的groupId和添加的商品信息
    * 返回Msg
     * by Xu
    * */
    @RequestMapping("/addItemToGroup")
    public Msg addItemToGroup(@RequestBody JSONObject ItemInfo)
    {
        int groupId = 0;
        try {
            groupId = ItemInfo.getInt("groupId");
            if (!authService.isGroupLeader(AuthUtil.getAuthUserId(),groupId)) return MsgUtil.makeMsg(MsgUtil.ERROR,"您没有权限往不属于自己的团购中添加商品");
            System.out.println(groupId);
        }catch (Exception err)
        {
            return MsgUtil.makeMsg(MsgUtil.ERROR,"groupId错误");
        }
        String itemName = ItemInfo.getString("itemName");
        int itemStock = ItemInfo.getInt("itemStock");
        int itemPrice = ItemInfo.getInt("itemPrice");
        String itemDescription = getItemDescription(ItemInfo);
        boolean itemSeckill =  ItemInfo.getBoolean("itemSeckill");
        Timestamp secKillStartTime = null,secKillEndTime =null;
        int flag = 0;
        if(itemSeckill)
        {
            flag = 1;
            secKillStartTime= TimeUtil.timeTrim(ItemInfo.getString("skStartTime"));
            secKillEndTime= TimeUtil.timeTrim(ItemInfo.getString("skEndTime"));
        }

        String [] url = new String[constant.IMAGE_NUM];
        getImage(ItemInfo, url);
        try {
            Item tmp = itemService.InsertOneItem(itemName,
                    groupId,
                    itemPrice,
                    url,
                    itemStock,
                    flag,
                    itemDescription,
                    secKillStartTime,
                    secKillEndTime);
            return MsgUtil.makeMsg(MsgUtil.SUCCESS,"添加商品成功!itemId为"+tmp.getItemId());
        }catch (Exception exception)
        {
            return MsgUtil.makeMsg(MsgUtil.ERROR,"添加商品错误！");
        }

    }
    /**
    * 传入对应的itemId即可删除
    * by Xu
    * */
    @RequestMapping("/deleteItemByID/{itemID}")
    public Msg deleteItemByID(@PathVariable("itemID") Integer itemID)
    {
       try {
           itemService.deleteItemByID(itemID);
           return MsgUtil.makeMsg(MsgUtil.SUCCESS,"删除成功！");
       }
       catch (Exception exception)
       {
           return MsgUtil.makeMsg(MsgUtil.ERROR,"删除商品异常！请联系程序员debug！");
       }
    }
    /*团购的删除，，订单的删，改（团长和团员）*/

    /**
     * 传入ItemInfo,所有item的信息
     * 返回一个msg
     * by Xu
     */
    @RequestMapping("/modifyItemInfo")
    public Msg modifyItemInfo(@RequestBody JSONObject ItemInfo )
    {
        int itemId = ItemInfo.getInt("itemId");
        if (!authService.isItemLeader(AuthUtil.getAuthUserId(),itemId)) return MsgUtil.makeMsg(MsgUtil.ERROR,"您没有权限修改不属于自己的团购中的商品");
        String itemName = ItemInfo.getString("itemName");
        int itemStock = ItemInfo.getInt("itemStock");
        int itemPrice = ItemInfo.getInt("itemPrice");
        boolean itemSeckill =  ItemInfo.getBoolean("itemSeckill");
        String itemDescription = getItemDescription(ItemInfo);
        String [] url = new String[constant.IMAGE_NUM];
        Timestamp secKillStartTime = null,secKillEndTime =null;
        int flag = 0;
        if(itemSeckill)
        {
            flag = 1;
            secKillStartTime= TimeUtil.timeTrim(ItemInfo.getString("skStartTime"));
            secKillEndTime= TimeUtil.timeTrim(ItemInfo.getString("skEndTime"));
        }
        getImage(ItemInfo, url);
        try {
            itemService.modifyItemInfo(itemId,itemName,itemPrice,url,itemStock,flag,itemDescription,secKillStartTime,secKillEndTime);
        }catch (Error e)
        {
            return MsgUtil.makeMsg(MsgUtil.ERROR,"修改商品信息出现错误！");
        }
        return MsgUtil.makeMsg(MsgUtil.SUCCESS,"修改商品信息成功！");
    }

    /**
     * @param ItemInfo
     * @param url
     * 获取图片url的工具函数
     * by Xu
     */
    private void getImage(@RequestBody JSONObject ItemInfo, String[] url) {
        for (int i = 0; i < constant.IMAGE_NUM; ++ i) {
            try {
                List<Map<String, String>> image = ItemInfo.getJSONArray("itemImage");
                url[i] = image.get(i).get("url");
            } catch (Exception e) {
                url[i] = null;
            }
        }
    }

    /**
     * @param ItemInfo
     * @return itemDescription
     * 获取itemDescription的工具函数
     * by Xu
     */
     private String getItemDescription(@RequestBody JSONObject ItemInfo)
     {
         String itemDescription;
         try {

             itemDescription = ItemInfo.getString("itemDescription");
             System.out.println(itemDescription);
         }catch (Exception e)
         {
             itemDescription = null;
         }
         return itemDescription;
     }
}

