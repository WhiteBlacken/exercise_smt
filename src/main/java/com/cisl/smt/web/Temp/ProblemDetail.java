package com.cisl.smt.web.Temp;

public class ProblemDetail implements Comparable<ProblemDetail>{
    /**
     * @description: 详细题目类型
     * @author: Hopenx
     * @date: 2020/12/29 10:53 下午
     */

    private Long prob_id;

    private String options;
    private String prob_text;
    private String prob_attr;
    private String prob_level;
    private String prob_diff;

    private Long lesson_id;
    private String point_text;
    private Long point_id;
    private Long blank_num;

    private String answer_text;
    private String analysis;

    public Long getProb_id() {
        return prob_id;
    }

    public void setProb_id(Long prob_id) {
        this.prob_id = prob_id;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getProb_text() {
        return prob_text;
    }

    public void setProb_text(String prob_text) {
        this.prob_text = prob_text;
    }

    public Long getPoint_id() {
        return point_id;
    }

    public void setPoint_id(Long point_id) {
        this.point_id = point_id;
    }

    public String getProb_attr() {
        return prob_attr;
    }

    public void setProb_attr(String prob_attr) {
        this.prob_attr = prob_attr;
    }

    public String getProb_level() {
        return prob_level;
    }

    public void setProb_level(String prob_level) {
        this.prob_level = prob_level;
    }

    public String getProb_diff() {
        return prob_diff;
    }

    public void setProb_diff(String prob_diff) {
        this.prob_diff = prob_diff;
    }

    public Long getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(Long lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getPoint_text() {
        return point_text;
    }

    public void setPoint_text(String point_text) {
        this.point_text = point_text;
    }

    public Long getBlank_num() {
        return blank_num;
    }

    public void setBlank_num(Long blank_num) {
        this.blank_num = blank_num;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    @Override
    public String toString() {
        return "ProblemDetail{" +
                "prob_id=" + prob_id +
                ", options='" + options + '\'' +
                ", prob_text='" + prob_text + '\'' +
                ", prob_attr='" + prob_attr + '\'' +
                ", prob_level='" + prob_level + '\'' +
                ", prob_diff='" + prob_diff + '\'' +
                ", lesson_id=" + lesson_id +
                ", point_text='" + point_text + '\'' +
                ", point_id=" + point_id +
                ", blank_num=" + blank_num +
                ", answer_text='" + answer_text + '\'' +
                ", analysis='" + analysis + '\'' +
                '}';
    }

    @Override
    public int compareTo(ProblemDetail o) {
        if (Integer.parseInt(this.prob_level) > Integer.parseInt(o.getProb_level()))
            return 1;
        else if (Integer.parseInt(this.prob_level) < Integer.parseInt(o.getProb_level()))
            return 0;
        else {
            if (this.lesson_id > o.getLesson_id())
                return 1;
            else
                return 0;
        }
    }
}
