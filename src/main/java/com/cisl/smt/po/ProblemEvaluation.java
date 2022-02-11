package com.cisl.smt.po;

import javax.persistence.*;

@Entity
@Table(name = "t_prob_eval")
public class ProblemEvaluation {

    @Id
    @GeneratedValue
    private Long prob_eval_id;

    private Long prob_id;
    private Long user_id;
    private String choice;
    private String choice_text;
    private String prob_text;
    private String analysis;
    private String ans;
    private String point;
    private String type;

    private int prob_eval_res;   //本题做对做错结果，0 做错，1 做对
    private String prob_eval_time;

    private Long consume_time;

    public ProblemEvaluation() {};

    public Long getProb_eval_id() {
        return prob_eval_id;
    }

    public void setProb_eval_id(Long prob_eval_id) {
        this.prob_eval_id = prob_eval_id;
    }

    public Long getProb_id() {
        return prob_id;
    }

    public void setProb_id(Long prob_id) {
        this.prob_id = prob_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getChoice_text() {
        return choice_text;
    }

    public void setChoice_text(String choice_text) {
        this.choice_text = choice_text;
    }

    public String getProb_text() {
        return prob_text;
    }

    public void setProb_text(String prob_text) {
        this.prob_text = prob_text;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getProb_eval_res() {
        return prob_eval_res;
    }

    public void setProb_eval_res(int prob_eval_res) {
        this.prob_eval_res = prob_eval_res;
    }

    public String getProb_eval_time() {
        return prob_eval_time;
    }

    public void setProb_eval_time(String prob_eval_time) {
        this.prob_eval_time = prob_eval_time;
    }

    public Long getConsume_time() {
        return consume_time;
    }

    public void setConsume_time(Long consume_time) {
        this.consume_time = consume_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProblemEvaluation{" +
                "prob_eval_id=" + prob_eval_id +
                ", prob_id=" + prob_id +
                ", user_id=" + user_id +
                ", choice='" + choice + '\'' +
                ", choice_text='" + choice_text + '\'' +
                ", prob_text='" + prob_text + '\'' +
                ", analysis='" + analysis + '\'' +
                ", ans='" + ans + '\'' +
                ", point='" + point + '\'' +
                ", type='" + type + '\'' +
                ", prob_eval_res=" + prob_eval_res +
                ", prob_eval_time='" + prob_eval_time + '\'' +
                ", consume_time=" + consume_time +
                '}';
    }
}
