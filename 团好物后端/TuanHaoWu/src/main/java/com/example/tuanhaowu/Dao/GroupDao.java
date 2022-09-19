package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.Group;

import java.util.List;

public interface GroupDao {
    List<Group> searchGroupByGroupLeader(String GroupLeader);
    List<Group> searchAllGroup();
    Group saveOneGroup(Group newGroup);
    Group getGroupByID(Integer groupID);
    Group searchGroupByGroupId(int groupId);
    List<Group> FuzzyQueryByGroupLeader(String groupLeader);
    List<Group> FuzzyQueryByGroupTitle(String groupTitle);
    List<Group> FuzzyQueryByItemName(String itemName);

}
