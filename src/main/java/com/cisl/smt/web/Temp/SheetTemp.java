package com.cisl.smt.web.Temp;



import java.util.ArrayList;

public class SheetTemp {
    /**
     * @description: 答题卡数据临时类
     * @author: Hopenx
     * @date: 2020.10.19
     */

    private Long id;   //此处 id 为试卷 id，当前毫秒数
    private ArrayList<ProblemAnsTemp> sheet_list;
    private ArrayList<Long> num_list;
    private Long opt_num;
    private Long txt_num;

    private String exer_level;
    private String start_time;
    private String end_time;
    private boolean evaluated;

    private int opt_choice_num;
    private int opt_tingyinxuanwen_num;
    private int opt_kantuxuanyin_num;
    private int opt_tingyinxuanci_num;
    private int opt_panduanzhengwu_num;
    private int txt_fill_num;

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

    public Long getOpt_num() {
        return opt_num;
    }

    public void setOpt_num(Long opt_num) {
        this.opt_num = opt_num;
    }

    public Long getTxt_num() {
        return txt_num;
    }

    public void setTxt_num(Long txt_num) {
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


    public int getOpt_choice_num() {
        return opt_choice_num;
    }

    public void setOpt_choice_num(int opt_choice_num) {
        this.opt_choice_num = opt_choice_num;
    }

    public int getOpt_tingyinxuanwen_num() {
        return opt_tingyinxuanwen_num;
    }

    public void setOpt_tingyinxuanwen_num(int opt_tingyinxuanwen_num) {
        this.opt_tingyinxuanwen_num = opt_tingyinxuanwen_num;
    }

    public int getOpt_kantuxuanyin_num() {
        return opt_kantuxuanyin_num;
    }

    public void setOpt_kantuxuanyin_num(int opt_kantuxuanyin_num) {
        this.opt_kantuxuanyin_num = opt_kantuxuanyin_num;
    }

    public int getOpt_tingyinxuanci_num() {
        return opt_tingyinxuanci_num;
    }

    public void setOpt_tingyinxuanci_num(int opt_tingyinxuanci_num) {
        this.opt_tingyinxuanci_num = opt_tingyinxuanci_num;
    }

    public int getOpt_panduanzhengwu_num() {
        return opt_panduanzhengwu_num;
    }

    public void setOpt_panduanzhengwu_num(int opt_panduanzhengwu_num) {
        this.opt_panduanzhengwu_num = opt_panduanzhengwu_num;
    }

    public int getTxt_fill_num() {
        return txt_fill_num;
    }

    public void setTxt_fill_num(int txt_fill_num) {
        this.txt_fill_num = txt_fill_num;
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
                ", opt_choice_num=" + opt_choice_num +
                ", opt_tingyinxuanwen_num=" + opt_tingyinxuanwen_num +
                ", opt_kantuxuanyin_num=" + opt_kantuxuanyin_num +
                ", opt_tingyinxuanci_num=" + opt_tingyinxuanci_num +
                ", opt_panduanzhengwu_num=" + opt_panduanzhengwu_num +
                ", txt_fill_num=" + txt_fill_num +
                '}';
    }
}
