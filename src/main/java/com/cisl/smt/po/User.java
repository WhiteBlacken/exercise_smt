package com.cisl.smt.po;

import javax.persistence.*;

@Entity
@Table(name = "t_user")
public class User {
    /**
     * @description: 用户信息表
     * @author: Hopenx
     * @date: 2020-10-23 08:11
     */

    @Id
    @GeneratedValue
    private Long user_id;

    @Column(unique = true)
    private String username;
    private String password;

    public User() {};

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
