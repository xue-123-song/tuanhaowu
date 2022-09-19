package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Integer> {
    @Query(value = "from Item where belongGroupId = :groupId ")
    List<Item> getAllByBelongGroupId(@Param("groupId") Integer groupId);

    @Query(value = "from Item where itemId = :itemId")
    Item getItemByItemId(@Param("itemId") Integer itemId);

    /**
     * 查询给定团购的所有itemId
     */
    @Query(value = "select obj.itemId from  Item obj where  obj.belongGroupId = :groupId")
    List<Integer> getItemIdByGroupId(@Param("groupId") Integer groupId);

}
