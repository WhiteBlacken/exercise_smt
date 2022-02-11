package com.cisl.smt.web.Temp;


public class SettingTemp {
    /**
     * @description: 保存出题组卷的参数
     * @author: Hopenx
     * @date: 2020-11-29 17:22
     */

    private String src;
    private String sys;   // 附加属性
    private Integer lev;
    private Integer probNum;
    private static SettingTemp instance = null;

    private SettingTemp() {}  // 也是一个全局的单例模式

    public static SettingTemp getInstance() {
        if(instance == null) {
            instance = new SettingTemp();
        }
        return instance;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public Integer getLev() {
        return lev;
    }

    public void setLev(Integer lev) {
        this.lev = lev;
    }

    public Integer getProbNum() {
        return probNum;
    }

    public void setProbNum(Integer probNum) {
        this.probNum = probNum;
    }

    @Override
    public String toString() {
        return "SettingTemp{" +
                "src='" + src + '\'' +
                ", sys='" + sys + '\'' +
                ", lev=" + lev +
                ", probNum=" + probNum +
                '}';
    }
}
