package com.cisl.smt.web.Temp;

/**
 * @Author qxy
 * @Date 2022/2/11 16:33
 * @Version 1.0
 */

public class FileInfo {
    //特别用于文件上传的返回类型
    private String info;
    private String option;
    private String url;

    public FileInfo() {
    }

    public FileInfo(String info, String option, String url) {
        this.info = info;
        this.option = option;
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "info='" + info + '\'' +
                ", option='" + option + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
