package com.cisl.smt.po;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_exer_eval")
public class ExerciseEvaluation {
    //TODO 加上一个 type 类型 本次练习是什么属性

    @Id
    @GeneratedValue
    private Long exer_eval_id;

    private Long exercise_id;
    private Long user_id;
    private Long exer_eval_score;
    private Long consume_time;

    private String exer_eval_time;
    private String exer_level;

    public ExerciseEvaluation() {};

    public Long getExer_eval_id() {
        return exer_eval_id;
    }

    public void setExer_eval_id(Long exer_eval_id) {
        this.exer_eval_id = exer_eval_id;
    }

    public Long getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(Long exercise_id) {
        this.exercise_id = exercise_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getExer_eval_score() {
        return exer_eval_score;
    }

    public void setExer_eval_score(Long exer_eval_score) {
        this.exer_eval_score = exer_eval_score;
    }

    public Long getConsume_time() {
        return consume_time;
    }

    public void setConsume_time(Long consume_time) {
        this.consume_time = consume_time;
    }

    public String getExer_eval_time() {
        return exer_eval_time;
    }

    public void setExer_eval_time(String exer_eval_time) {
        this.exer_eval_time = exer_eval_time;
    }

    public String getExer_level() {
        return exer_level;
    }

    public void setExer_level(String exer_level) {
        this.exer_level = exer_level;
    }

    @Override
    public String toString() {
        return "ExerciseEvaluation{" +
                "exer_eval_id=" + exer_eval_id +
                ", exercise_id=" + exercise_id +
                ", user_id=" + user_id +
                ", exer_eval_score=" + exer_eval_score +
                ", consume_time=" + consume_time +
                ", exer_eval_time='" + exer_eval_time + '\'' +
                ", exer_level='" + exer_level + '\'' +
                '}';
    }
}
