package com.cisl.smt.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author qxy
 * @Date 2022/4/26 21:52
 * @Version 1.0
 */
@Entity
@Table(name = "t_lesson")
public class Lesson {

    @Id
    @GeneratedValue
    private int id;

    private int level_num;
    private int lesson_num;

    private String type;


    public Lesson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel_num() {
        return level_num;
    }

    public void setLevel_num(int level_num) {
        this.level_num = level_num;
    }

    public int getLesson_num() {
        return lesson_num;
    }

    public void setLesson_num(int lesson_num) {
        this.lesson_num = lesson_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
