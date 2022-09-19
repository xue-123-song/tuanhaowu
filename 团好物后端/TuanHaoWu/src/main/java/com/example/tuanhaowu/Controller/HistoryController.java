package com.example.tuanhaowu.Controller;

import com.example.tuanhaowu.Entity.History;
import com.example.tuanhaowu.Service.HistoryService;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    /**
     * 增
     * by Xu
     */
    @RequestMapping("/history/add")
    public Msg addHistory(@RequestBody JSONObject info)
    {
        try {
            String userId = info.getString("belongUserId");
            Integer groupId = info.getInt("groupId");
            historyService.add(userId,groupId);
            return MsgUtil.makeMsg(MsgUtil.SUCCESS,"添加历史记录成功");
        }catch (Exception e)
        {
            return MsgUtil.makeMsg(MsgUtil.ERROR,"添加历史记录异常");
        }

    }
    /**
     * 删
     * by Xu
     */
    @RequestMapping("/history/deleteOne/{historyId}")
    public Msg deleteOneHistory(@PathVariable("historyId") Integer historyId)
    {
        if (historyService.deleteOne(historyId) >= 0)
        return MsgUtil.makeMsg(MsgUtil.SUCCESS,"删除历史记录成功");
        else return MsgUtil.makeMsg(MsgUtil.ERROR,"删除历史记录错误");
    }
    /*
     * 改，历史记录不能改，如有需要，请联系我 ---by Xu
     */
    /**
     * 查：返回某个用户的所有历史记录
     * by Xu
     */
    @RequestMapping("/history/QueryAll/{userId}")
    public List<History> QueryAllHistory(@PathVariable("userId") String userId)
    {
        return historyService.QueryAll(userId);
    }
    /**
     * 清空：删除全部记录
     * by Xu
     */
    @RequestMapping("/history/clearAll/{userId}")
    public Msg clearAllHistory(@PathVariable("userId")String userId)
    {
        if (historyService.clearAll(userId) >= 0)
        return MsgUtil.makeMsg(MsgUtil.SUCCESS,"清空历史记录成功");
        else return MsgUtil.makeMsg(MsgUtil.SUCCESS,"清空历史记录失败");
    }

}
