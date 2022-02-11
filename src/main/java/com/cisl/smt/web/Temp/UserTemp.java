package com.cisl.smt.web.Temp;

public class UserTemp {
    /**
     * @description: 登录保存的用户信息类
     * @author: Hopenx
     * @date: 2021/3/2 5:49 下午
     */

    private String user_id;
    private String user_seq;
    private String level;

    public UserTemp(String user_id, String user_seq, String level) {
        this.user_id = user_id;
        this.user_seq = user_seq;
        this.level = level;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_seq() {
        return user_seq;
    }

    public void setUser_seq(String user_seq) {
        this.user_seq = user_seq;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "UserTemp{" +
                "user_id='" + user_id + '\'' +
                ", user_seq='" + user_seq + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
