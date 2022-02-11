package com.cisl.smt.web.Temp;

public class CommentTemp {
    /**
     * @description: 评卷结果综述
     * @author: Hopenx
     * @date: 2020-11-23 20:39
     */

    private String time = "2020-10-30 15:10:11";
    private int level = 1;
    private int score = 88;
    private int lesson = 0;
    private int avg_score = 88;
    private int handle_rate = 88;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getAvg_score() {
        return avg_score;
    }

    public void setAvg_score(int avg_score) {
        this.avg_score = avg_score;
    }

    public int getHandle_rate() {
        return handle_rate;
    }

    public void setHandle_rate(int handle_rate) {
        this.handle_rate = handle_rate;
    }

    @Override
    public String toString() {
        return "CommentTemp{" +
                "time='" + time + '\'' +
                ", level=" + level +
                ", score=" + score +
                ", lesson=" + lesson +
                ", avg_score=" + avg_score +
                ", handle_rate=" + handle_rate +
                '}';
    }
}
