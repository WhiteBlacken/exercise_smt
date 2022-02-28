package com.cisl.smt.web.Temp;



import java.util.ArrayList;
import java.util.List;

public class SheetTemp {
    /**
     * @description: 答题卡数据临时类
     * @author: Hopenx
     * @date: 2020.10.19
     */

    private Long id;   //此处 id 为试卷 id，当前毫秒数
    private ArrayList<ProblemAnsTemp> sheet_list;
    private ArrayList<Long> num_list;
    private int opt_num;
    private int txt_num;

    private String exer_level;
    private String start_time;
    private String end_time;
    private boolean evaluated;



    public SheetTemp(){
        ArrayList<ProblemAnsTemp> empty1 = new ArrayList<>();
        ArrayList<Long> empty2 = new ArrayList<>();
        this.setSheet_list(empty1);
        this.setNum_list(empty2);
        this.evaluated = false;
    };   //还要初始化自己的列表


    public void addSheet_list(ProblemAnsTemp pt) {
        if(this.sheet_list == null){
            ArrayList<ProblemAnsTemp> empty1 = new ArrayList<>();
            this.setSheet_list(empty1);
        }
        this.sheet_list.add(pt);
    }

    public void clearSheet_list() {
        this.sheet_list.clear();
        this.num_list.clear();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<ProblemAnsTemp> getSheet_list() {
        return sheet_list;
    }

    public void setSheet_list(ArrayList<ProblemAnsTemp> sheet_list) {
        this.sheet_list = sheet_list;
    }

    public ArrayList<Long> getNum_list() {
        return num_list;
    }

    public void setNum_list(ArrayList<Long> num_list) {
        this.num_list = num_list;
    }

    public int getOpt_num() {
        return opt_num;
    }

    public void setOpt_num(int opt_num) {
        this.opt_num = opt_num;
    }

    public int getTxt_num() {
        return txt_num;
    }

    public void setTxt_num(int txt_num) {
        this.txt_num = txt_num;
    }

    public String getExer_level() {
        return exer_level;
    }

    public void setExer_level(String exer_level) {
        this.exer_level = exer_level;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }


    @Override
    public String toString() {
        return "SheetTemp{" +
                "id=" + id +
                ", sheet_list=" + sheet_list +
                ", num_list=" + num_list +
                ", opt_num=" + opt_num +
                ", txt_num=" + txt_num +
                ", exer_level='" + exer_level + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", evaluated=" + evaluated +
                '}';
    }
}
