package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GroupRepository extends JpaRepository<Group,Integer> {
    @Query(value = "from Group where groupLeader = :GroupLeader and status >= 0 order by groupId desc ")
    List<Group> SearchAllByGroupLeader(@Param("GroupLeader") String GroupLeader);

    @Query(value = "from Group where groupId = :groupId")
    Group searchGroupByGroupId(@Param("groupId") Integer groupId);

    @Query(value = "from Group where groupLeader like  :groupLeader")
    List<Group>  FuzzyQueryByGroupLeader(@Param("groupLeader") String groupLeader);

    @Query(value = "from Group where groupTitle like  :groupTitle")
    List<Group>  FuzzyQueryByGroupTitle(@Param("groupTitle") String groupTitle);

    @Query(value = "from Group where groupId in (SELECT obj.belongGroupId from Item obj where obj.itemName like :itemName)")
    List<Group>  FuzzyQueryByItemName(@Param("itemName") String itemName);
}
