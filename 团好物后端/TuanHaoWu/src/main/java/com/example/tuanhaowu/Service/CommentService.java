package com.example.tuanhaowu.Service;

import com.example.tuanhaowu.Entity.Comment;
import com.example.tuanhaowu.util.MsgUtil.Msg;


import java.util.List;

public interface CommentService {
    /**
     * 增加一条评论
     * @return
     * by Xu
     */
    Comment addComment(String content,String fromUserId,String toUserId,Integer parentId,String leaderId);

    /**
     * 删除一条评论
     * @param commentId
     * @return
     * by Xu
     */
    int deleteComment(Integer commentId);

    /**
     * 查询父评论
     * @param leaderId
     * @return
     * by Xu
     */
    List<Comment> QueryParent(String leaderId);

    /**
     * 展开子评论
     * @param parentId
     * @return
     * by Xu
     */
    List<Comment> QueryChild(Integer parentId);
}
