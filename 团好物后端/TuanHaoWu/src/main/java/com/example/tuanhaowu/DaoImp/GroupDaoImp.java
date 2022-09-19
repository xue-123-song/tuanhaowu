package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.GroupDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Repository
public class GroupDaoImp implements GroupDao {
    @Autowired
    GroupRepository groupRepository;

//    @Override
//    public  Group CreateGroup(String gpLeader, String gpTitle, String gpDescription, Integer logistics, Timestamp startTime, Timestamp endTime) {
//        Group group = new Group();
//        group.setGroupLeader(gpLeader);
//        group.setGroupTitle(gpTitle);
//        group.setGroupDescription(gpDescription);
//        group.setLogistics(logistics);
//        group.setStartTime(startTime);
//        group.setEndTime(endTime);
//        return groupRepository.save(group);
//    }

    @Override
    public List<Group> searchGroupByGroupLeader(String GroupLeader) {
        return groupRepository.SearchAllByGroupLeader(GroupLeader);
    }

    @Override
    public List<Group> searchAllGroup() {
        return groupRepository.findAll();
    }
    @Override
    public Group saveOneGroup(Group newGroup){return groupRepository.save(newGroup);}

    @Override
    public Group getGroupByID(Integer groupID) {
        return groupRepository.getById(groupID);
    }

    @Override
    public Group searchGroupByGroupId(int groupId) {

        return groupRepository.searchGroupByGroupId(groupId);
    }

    @Override
    public List<Group> FuzzyQueryByGroupLeader(String groupLeader) {
        return groupRepository.FuzzyQueryByGroupLeader("%"+groupLeader+"%");
    }

    @Override
    public List<Group> FuzzyQueryByGroupTitle(String groupTitle) {
        return groupRepository.FuzzyQueryByGroupTitle("%"+groupTitle+"%");
    }
    @Override
    public List<Group>  FuzzyQueryByItemName(String itemName){
        return groupRepository.FuzzyQueryByItemName("%"+itemName+"%");
    }
}
