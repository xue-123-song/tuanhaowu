package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    @Query(value = "from History where belongUserId =:userId and historyStatus >= 0 order by visitTime" )
    List<History> QueryAll(@Param("userId") String userId);

    @Query(value = "select count(obj.groupId)  from History obj where obj.groupId =:groupId group by obj.groupId")
    int getVisitTimes(@Param("groupId") Integer groupId);

    @Query(value = "select count(obj.groupId) from History obj where obj.groupId =:groupId and " +
            "obj.visitTime >=:zero and obj.visitTime <=:now")
    int todayIncrementVisit(@Param("groupId") Integer groupId, @Param("zero")Timestamp zero,@Param("now") Timestamp now);
}
