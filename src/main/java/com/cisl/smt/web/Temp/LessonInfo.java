package com.cisl.smt.web.Temp;

import com.cisl.smt.po.Lesson;

import java.util.List;

/**
 * @Author qxy
 * @Date 2022/4/25 23:33
 * @Version 1.0
 */
public class LessonInfo {
    private boolean pre_btn;
    private boolean next_btn;
    private int max_lesson_num;
    private int max_lesson_this_page;
    private List<Lesson> lessons;

    public LessonInfo() {
    }

    public LessonInfo(boolean pre_btn, boolean next_btn, int max_lesson_num, List<Lesson> lessons,int max_lesson_this_page) {
        this.pre_btn = pre_btn;
        this.next_btn = next_btn;
        this.max_lesson_num = max_lesson_num;
        this.lessons = lessons;
        this.max_lesson_this_page = max_lesson_this_page;
    }

    public boolean isPre_btn() {
        return pre_btn;
    }

    public void setPre_btn(boolean pre_btn) {
        this.pre_btn = pre_btn;
    }

    public boolean isNext_btn() {
        return next_btn;
    }

    public void setNext_btn(boolean next_btn) {
        this.next_btn = next_btn;
    }

    public int getMax_lesson_num() {
        return max_lesson_num;
    }

    public void setMax_lesson_num(int max_lesson_num) {
        this.max_lesson_num = max_lesson_num;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public int getMax_lesson_this_page() {
        return max_lesson_this_page;
    }

    public void setMax_lesson_this_page(int max_lesson_this_page) {
        this.max_lesson_this_page = max_lesson_this_page;
    }
}
