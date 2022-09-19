package com.example.tuanhaowu.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Message {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "m_id")
    private int mId;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "message")
    private String message;
    @Basic
    @Column(name = "date")
    private Timestamp date;
    @Basic
    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return mId == message1.mId && Objects.equals(userId, message1.userId) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, userId, message);
    }
}
