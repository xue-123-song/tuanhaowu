package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Comment;

import java.util.List;

public interface CommentDao {
    Comment saveOneComment(Comment comment);
    void deleteComment(Integer commentId);
    Comment getComment(Integer commentId);
    List<Comment> QueryParent(String leaderId);
    List<Comment> QueryChild(Integer parentId);

}
