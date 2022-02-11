package com.cisl.smt.po;

import javax.persistence.*;

@Entity
@Table(name = "t_rev_collec")
public class ReviewCollection {

    @Id
    @GeneratedValue
    private Long rev_collec_id;

    private Long user_id;
    private String prob_eval_list;
    private String prob_list;

    public ReviewCollection() {};

    public Long getRev_collec_id() {
        return rev_collec_id;
    }

    public void setRev_collec_id(Long rev_collec_id) {
        this.rev_collec_id = rev_collec_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getProb_eval_list() {
        return prob_eval_list;
    }

    public void setProb_eval_list(String prob_eval_list) {
        this.prob_eval_list = prob_eval_list;
    }

    public String getProb_list() {
        return prob_list;
    }

    public void setProb_list(String prob_list) {
        this.prob_list = prob_list;
    }

    @Override
    public String toString() {
        return "ReviewCollection{" +
                "rev_collec_id=" + rev_collec_id +
                ", user_id=" + user_id +
                ", prob_eval_list='" + prob_eval_list + '\'' +
                ", prob_list='" + prob_list + '\'' +
                '}';
    }
}
