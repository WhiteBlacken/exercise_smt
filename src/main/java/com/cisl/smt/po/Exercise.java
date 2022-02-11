package com.cisl.smt.po;

import javax.persistence.*;

@Entity
@Table(name = "t_exercise")
public class Exercise {

    @Id
    @GeneratedValue
    private Long exercise_id;

    private String exercise_attr;
    private String prob_list;
    private Long grammar_id;
    private String update_time;

    public Exercise() {};

    public Long getExercise_id() {
        return exercise_id;
    }

    public String getExercise_attr() {
        return exercise_attr;
    }

    public String getProb_list() {
        return prob_list;
    }

    public Long getGrammar_id() {
        return grammar_id;
    }

    public void setGrammar_id(Long grammar_id) {
        this.grammar_id = grammar_id;
    }


    public void setExercise_id(Long exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setExercise_attr(String exercise_attr) {
        this.exercise_attr = exercise_attr;
    }

    public void setProb_list(String prob_list) {
        this.prob_list = prob_list;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exercise_id=" + exercise_id +
                ", exercise_attr='" + exercise_attr + '\'' +
                ", prob_list='" + prob_list + '\'' +
                ", grammar_id=" + grammar_id +
                ", update_time=" + update_time +
                '}';
    }
}
