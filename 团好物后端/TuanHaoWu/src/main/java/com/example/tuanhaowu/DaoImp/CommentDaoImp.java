package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.CommentDao;
import com.example.tuanhaowu.Entity.Comment;
import com.example.tuanhaowu.Repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
public class CommentDaoImp implements CommentDao {
    @Autowired
    CommentRepository commentRepository;
    @Override
    public Comment saveOneComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.getById(commentId);
        /*删除所有的子评论*/
        List<Comment> child = commentRepository.QueryChild(commentId);
        for (Comment i : child) {
            commentRepository.deleteById(i.getCommentId());
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment getComment(Integer commentId) {
        return commentRepository.getById(commentId);
    }

    @Override
    public List<Comment> QueryParent(String leaderId) {
        return commentRepository.QueryParent(leaderId);
    }

    @Override
    public List<Comment> QueryChild(Integer parentId) {
        return commentRepository.QueryChild(parentId);
    }
}
