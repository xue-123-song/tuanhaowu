package com.example.tuanhaowu.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private int commentId;
    @Basic
    @Column(name = "content")
    private String content;     //评论内容
    @Basic
    @Column(name = "from_user_id")
    private String fromUserId;  //评论的用户id
    @Basic
    @Column(name = "to_user_id")
    private String toUserId;    //被评论的用户id
    @Basic
    @Column(name = "parent_id")
    private Integer parentId;    //是在哪个评论下的子评论
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;
    @Basic
    @Column(name = "leaderId")
    private String leaderId;    //是对哪个团长的评价，或者说在哪个团长的个人页面上进行的评价

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId == comment.commentId && Objects.equals(content, comment.content) && Objects.equals(fromUserId, comment.fromUserId) && Objects.equals(toUserId, comment.toUserId) && Objects.equals(parentId, comment.parentId) && Objects.equals(createTime, comment.createTime) && Objects.equals(leaderId, comment.leaderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, content, fromUserId, toUserId, parentId, createTime, leaderId);
    }
}
