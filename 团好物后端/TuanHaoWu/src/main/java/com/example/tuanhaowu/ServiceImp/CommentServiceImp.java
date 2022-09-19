package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.CommentDao;
import com.example.tuanhaowu.Entity.Comment;
import com.example.tuanhaowu.Service.CommentService;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements CommentService {
    @Autowired
    CommentDao commentDao;
    /**
     * 增加一条评论
     * @return
     * by Xu
     */
    @Override
    public Comment addComment(String content, String fromUserId, String toUserId, Integer parentId, String leaderId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setFromUserId(fromUserId);
        comment.setToUserId(toUserId);
        comment.setParentId(parentId);
        comment.setLeaderId(leaderId);
        comment.setCreateTime(TimeUtil.getNow());
        return commentDao.saveOneComment(comment) ;
    }
    /**
     * 删除一条评论
     * @param commentId
     * @return
     * by Xu
     */
    @Override
    public int deleteComment(Integer commentId) {
        try {
            commentDao.deleteComment(commentId);
            return 0;
        }catch (Exception e)
        {
            return -1;
        }
    }
    /**
     * 查询父评论
     * @param leaderId
     * @return
     * by Xu
     */
    @Override
    public List<Comment> QueryParent(String leaderId) {
        return commentDao.QueryParent(leaderId);
    }
    /**
     * 展开子评论
     * @param parentId
     * @return
     * by Xu
     */
    @Override
    public List<Comment> QueryChild(Integer parentId) {
        return commentDao.QueryChild(parentId);
    }
}
