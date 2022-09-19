package com.example.tuanhaowu.Repository;

import com.example.tuanhaowu.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
    @Query(value = "from User where account = :account")
    User checkUser(@Param("account") String account);

    @Query(value = "from User where userid = :userId")
    User getUserByUserid(@Param("userId") String userId);
    @Query(value = "from User where name = :name")
    User getUserByName(@Param("name") String name);

    @Query(value = "from  User where name = :name ")
    List<User> checkNameRepeated(@Param("name") String name) ;

    @Query(value = "from  User where account = :account ")
    List<User> checkAccountRepeated(@Param("account") String account);
}
