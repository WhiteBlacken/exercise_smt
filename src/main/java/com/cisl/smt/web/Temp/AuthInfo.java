package com.cisl.smt.web.Temp;

public class AuthInfo {
    /**
     * @description:  跳转链接认证类
     * @author: Hopenx
     * @date: 2021/3/5 7:00 下午
     */

    private String user_seq;
    private String user_id;
    private String level;
    private String token;

    public AuthInfo(){}  //必须要空的构造函数，才能解析 JSON

    public String getUser_seq() {
        return user_seq;
    }

    public void setUser_seq(String user_seq) {
        this.user_seq = user_seq;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "user_seq='" + user_seq + '\'' +
                ", user_id='" + user_id + '\'' +
                ", level='" + level + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
