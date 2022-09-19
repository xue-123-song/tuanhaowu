package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query(value = "from Message where userId = :userId")
    List<Message> getMessageByUserId(@Param("userId") String userId);
}
