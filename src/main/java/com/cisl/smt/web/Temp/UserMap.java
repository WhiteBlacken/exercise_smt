package com.cisl.smt.web.Temp;

import java.util.HashMap;

public class UserMap {
    /**
     * @description: 登录全局类, 一个 token 对应一个 User
     * @author: Hopenx
     * @date: 2021/3/2 5:47 下午
     */

    private static UserMap instance = null;
    private HashMap<String, UserTemp> tempHashMap;

    private UserMap(){};   //单例模式，外部不能 new

    public static UserMap getInstance() {
        if(instance == null){
            instance = new UserMap();
            instance.setTempHashMap(new HashMap<String, UserTemp>());
        }
        return instance;
    }

    public HashMap<String, UserTemp> getTempHashMap() {
        return tempHashMap;
    }

    public void setTempHashMap(HashMap<String, UserTemp> tempHashMap) {
        this.tempHashMap = tempHashMap;
    }

    @Override
    public String toString() {
        return "UserMap{" +
                "tempHashMap=" + tempHashMap +
                '}';
    }
}
