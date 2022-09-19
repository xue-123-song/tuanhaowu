//package com.example.tuanhaowu.Controller;
//
//import com.example.tuanhaowu.Entity.Comment;
//import com.example.tuanhaowu.Service.CommentService;
//import com.example.tuanhaowu.util.MsgUtil.Msg;
//import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class CommentController {
//    @Autowired
//    CommentService commentService;
//    /**
//     * 增 加一条评论
//     * @param info
//     * @return
//     * by Xu
//     */
//    @RequestMapping(value = "/Comment/addComment")
//    public Msg addComment(@RequestBody JSONObject info)
//    {
//        Integer parentId;
//        String fromUserId ,toUserId ,leaderId ,content;
//        try {
//            content = info.getString("content");
//            fromUserId = info.getString("fromUserId");
//            leaderId = info.getString("leaderId");
//        }catch (Exception e)
//        {
//            return MsgUtil.makeMsg(MsgUtil.ERROR,"评论缺少必填信息");
//        }
//        try {
//            toUserId = info.getString("toUserId");
//        }catch (Exception e)
//        {
//            toUserId = null;
//        }
//        try {
//             parentId = info.getInt("parentId");
//        }catch (Exception e)
//        {
//            parentId = null;
//        }
//        try {
//            commentService.addComment(content,fromUserId,toUserId,parentId,leaderId);
//            return MsgUtil.makeMsg(MsgUtil.SUCCESS,"评论成功");
//        }catch (Exception e)
//        {
//            return MsgUtil.makeMsg(MsgUtil.ERROR,"评论失败");
//        }
//
//    }
//
//    /**
//     * 删 除一条评论
//     * @param commentId
//     * @return
//     */
//    @RequestMapping(value = "/Comment/deleteComment/{commentId}")
//    public Msg deleteComment(@PathVariable("commentId") Integer commentId)
//    {
//           if (commentService.deleteComment(commentId) >=0)
//            return MsgUtil.makeMsg(MsgUtil.SUCCESS,"删除评论成功") ;
//           else return MsgUtil.makeMsg(MsgUtil.ERROR,"删除评论失败");
//    }
//    /*改 评论不可改*/
//
//    /**
//     * 查 看所有的父评论
//     * @return
//     */
//    @RequestMapping(value = "/Comment/QueryParent/{leaderId}")
//    public List<Comment> QueryParent(@PathVariable("leaderId") String leaderId)
//    {
//        return commentService.QueryParent(leaderId);
//    }
//
//    /**
//     * 查 展开某个多人评论
//     * @param parentId
//     * @return
//     */
//    @RequestMapping(value = "/Comment/QueryChild/{parentId}")
//    public List<Comment> QueryChild(@PathVariable("parentId") Integer parentId)
//    {
//        return commentService.QueryChild(parentId);
//    }
//
//
//}
