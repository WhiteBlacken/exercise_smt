package com.cisl.smt.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_rep_collec")
public class RepeatCollection {

    @Id
    @GeneratedValue
    private Long rep_collec_id;
    private Long user_id;
    private String prob_list;

    public RepeatCollection() {};

    public Long getRep_collec_id() {
        return rep_collec_id;
    }

    public void setRep_collec_id(Long rep_collec_id) {
        this.rep_collec_id = rep_collec_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getProb_list() {
        return prob_list;
    }

    public void setProb_list(String prob_list) {
        this.prob_list = prob_list;
    }

    @Override
    public String toString() {
        return "RepeatCollection{" +
                "rep_collec_id=" + rep_collec_id +
                ", user_id=" + user_id +
                ", prob_list='" + prob_list + '\'' +
                '}';
    }
}
