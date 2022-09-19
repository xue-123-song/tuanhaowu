package com.example.tuanhaowu.util;

import com.example.tuanhaowu.Dao.GroupDao;
import com.example.tuanhaowu.Dao.ItemDao;
import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.Item;
import com.example.tuanhaowu.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class recommendUtil {
    @Autowired
    ItemDao itemDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    UserDao userDao;

    public List<Group> getLatestGroup()
    {
        List<Group> groupList = groupDao.searchAllGroup();
        groupList.sort(((o1, o2) -> -o1.getStartTime().compareTo(o2.getStartTime())));
        groupList = groupList.subList(0,8);
        return  groupList;
    }


    public List<Item> getItem(List<Integer> idList)
    {
        List<Item> itemList = new ArrayList<>();
        for(Integer integer:idList)
        {
            Item item = itemDao.getItemByItemId(integer);
            itemList.add(item);
        }
        return itemList;
    }


    public List<Integer> recommendByNumber()
    {
        List<Item> itemList = itemDao.getAll();
        List<Integer> idList = new ArrayList<>();
        itemList.sort(((o1, o2) -> -o1.getNumber().compareTo(o2.getNumber())));
        int i = 0;
        while (idList.size() < 8)
        {
            int j = itemList.get(i).getItemId();
            idList.add(j);
            i++;
        }
        return  idList;
    }


    public List<Integer> recommend(String user_id)
    {
        List<Item> itemList = itemDao.getAll();
        List<User> userList = userDao.getAll();
        List<Item> items = itemDao.getItemByUserID(user_id);
        if(!userList.contains(user_id))
            return recommendByNumber();
        int size = itemList.size();
        double [][] curMatrix = new double[size + 2][size + 2];
        double [][] comMatrix = new double[size + 2][size + 2];
        int [] N = new int[size + 2];
        double [] rank = new double[size + 2];

        for (int i = 0;i < size;i++)
        {
            N[i] = itemList.get(i).getNumber();
            rank[i] = 0.0;
        }

        for(User user:userList)
        {
            if (user.getUserid() == user_id) continue;

            items = itemDao.getItemByUserID(user.getUserid());
            if (items == null) continue;

            for(int i = 0;i < size;i++)
                for (int j = 0;j < size;j++)
                    curMatrix[i][j] = 0.0;


            for (int i = 0;i < items.size();i++)
            {
                int id_1 = items.get(i).getItemId();
                for (int j = i + 1;j < items.size();j++)
                {
                    int id_2 = items.get(j).getItemId();
                    double Wij = 1 / Math.log(1 + items.size() * 1.0);
                    curMatrix[id_1][id_2] += Wij;
                    curMatrix[id_2][id_1] += Wij;
                }
            }
            //累加所有矩阵, 得到共现矩阵,进一步得到相似度矩阵
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    comMatrix[i][j] += curMatrix[i][j];
                }
            }
        }

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                comMatrix[i][j] = comMatrix[i][j] / Math.sqrt(N[i] * N[j]);
            }
        }

        items = itemDao.getItemByUserID(user_id);
        List<Integer> itemsID = new ArrayList<>();
        if (items != null)
            for (Item value : items) itemsID.add(value.getItemId());
        Map<Integer,Double> map = new HashMap<>();

        if(items != null)
        {
            for (Item item:items)
            {
                int id_1 = item.getItemId();
                map.clear();
                for (int i = 0;i < size;i++)
                {
                    if (itemsID.contains(i)) continue;
                    if (comMatrix[id_1][i] == 0.0) continue;
                    map.put(i,comMatrix[id_1][i]);
                }
                List<Map.Entry<Integer,Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
                entryList = Mysort(entryList);
                for (int i = 0;i < 10 && i < entryList.size();i++)
                    rank[entryList.get(i).getKey()] += entryList.get(i).getValue();
            }
        }

        map.clear();
        for (int i = 0;i < size;i++)
        {
            if (rank[i] != 0.0)
                map.put(i,rank[i]);
        }

        List<Map.Entry<Integer,Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
        entryList = Mysort(entryList);
        List<Integer> idList = new ArrayList<>();
        for (int i = 0;i < entryList.size() && i < 5;i++)
            idList.add(entryList.get(i).getKey());
        if (idList.size() < 8)
        {
            itemList.sort(new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return -o1.getNumber().compareTo(o2.getNumber());
                }
            });
            int i = 0;
            while (idList.size() < 8)
            {
                int j = itemList.get(i).getItemId();
                if (map.containsKey(j))
                {
                    i++;
                    continue;
                }
                idList.add(j);
                i++;
            }
        }
        Collections.shuffle(idList);//打乱推荐顺序
        return idList;
    }

    public List<Map.Entry<Integer,Double>> Mysort(List<Map.Entry<Integer,Double>> entryList)
    {
        entryList.sort(new Comparator<Map.Entry<Integer, Double>>() {
            @Override
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        return  entryList;
    }

}
