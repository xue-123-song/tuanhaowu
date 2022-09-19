package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query(value = "from Comment where leaderId =:leaderId and toUserId =:leaderId")
    List<Comment> QueryParent(@Param("leaderId") String leaderId);

    @Query(value = "from Comment where parentId =:parentId")
    List<Comment> QueryChild(@Param("parentId") Integer parentId);
}
