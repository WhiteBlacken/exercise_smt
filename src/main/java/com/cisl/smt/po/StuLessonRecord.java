package com.cisl.smt.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author qxy
 * @Date 2022/4/26 22:01
 * @Version 1.0
 */
@Entity
@Table(name = "t_stu_lesson")
public class StuLessonRecord{

    @Id
    @GeneratedValue
    private Long id;

    private Long user_id;
    private int lesson_id;


    public StuLessonRecord() {
    }

    public StuLessonRecord(Long user_id, int lesson_id) {
        this.id = id;
        this.user_id = user_id;
        this.lesson_id = lesson_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }
}