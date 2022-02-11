package com.cisl.smt.web.Temp;

import java.util.HashMap;

public class SheetMap {
    /**
     * @description: 当前在线做题用户的全局数据 HashMap，一个 user_id 对应一个 SheetTemp
     * @author: Hopenx
     * @date: 2021/1/6 7:37 上午
     */

    private static SheetMap instance = null;
    private HashMap<Long, SheetTemp> tempHashMap;

    private SheetMap(){};   //单例模式，外部不能 new

    public static SheetMap getInstance() {
        if(instance == null){
            instance = new SheetMap();
            instance.setTempHashMap(new HashMap<Long, SheetTemp>());
        }
        return instance;
    }

    public HashMap<Long, SheetTemp> getTempHashMap() {
        return tempHashMap;
    }

    public void setTempHashMap(HashMap<Long, SheetTemp> tempHashMap) {
        this.tempHashMap = tempHashMap;
    }

    @Override
    public String toString() {
        return "SheetMap{" +
                "tempHashMap=" + tempHashMap +
                '}';
    }
}
