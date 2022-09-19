package com.example.tuanhaowu.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * step6：这里我继承了Spring-Security中的user类，给user在原有的实现上增加了一些用于验证的属性，不过现阶段这些属性还没有很好的利用起来，因为这些
 * 属性都是框架自带必须写出来的属性，这样在验证的时候才好直接进行验证
 */
@Entity
@Table(name="user")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class User implements UserDetails {
    @Id
    @Column(name = "userid")
    private String userid;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "account")
    private String account;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "picture")
    private String picture;
    @Basic
    @Column(name = "money")
    private int money;

    @Basic
    @Column(name = "tel")
    private String tel;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "usercid")
    private String userCid;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String email) {
        this.address = email;
    }

    public String getUserCid() {
        return userCid;
    }

    public void setUserCid(String userCid) {
        this.userCid = userCid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userid == user.userid && Objects.equals(name, user.name) && Objects.equals(account, user.account) && Objects.equals(password, user.password) && Objects.equals(picture, user.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, name, account, password, picture);
    }

    public String getRole() {
        return "User";
    }
}
