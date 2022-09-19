package com.example.tuanhaowu.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class History {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "history_id")
    private int historyId;
    @Basic
    @Column(name = "belong_user_id")
    private String belongUserId;
    @Basic
    @Column(name = "group_id")
    private Integer groupId;
    @Basic
    @Column(name = "visit_time")
    private Timestamp visitTime;
    /**
     * 0代表存在，-1代表被删除了
     */
    @Basic
    @Column(name= "history_status")
    private int historyStatus;

    public int getHistoryStatus() {
        return historyStatus;
    }

    public void setHistoryStatus(int historyStatus) {
        this.historyStatus = historyStatus;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Timestamp getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Timestamp visitTime) {
        this.visitTime = visitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return historyId == history.historyId && Objects.equals(belongUserId, history.belongUserId) && Objects.equals(groupId, history.groupId) && Objects.equals(visitTime, history.visitTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyId, belongUserId, groupId, visitTime);
    }
}
