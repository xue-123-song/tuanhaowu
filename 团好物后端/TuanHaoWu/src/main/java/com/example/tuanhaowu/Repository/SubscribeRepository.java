package com.example.tuanhaowu.Repository;


import com.example.tuanhaowu.Entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe,Integer> {
    @Query(value = "from Subscribe where groupId = :groupId")
    List<Subscribe> getAllByGroupId(@Param("groupId") Integer groupId);

    @Query(value = "select obj.subscribeId from Subscribe obj where obj.subscribeUserId = :userId and obj.groupId = :groupId")
    Integer get_SubscribeId_ByUserIdAndGroupId(@Param("userId") String userId,@Param("groupId") Integer groupId);
}
