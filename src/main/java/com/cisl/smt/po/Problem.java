package com.cisl.smt.po;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_problem")
public class Problem {

    @Id
    @GeneratedValue
    private Long prob_id;

    private Long options_id;
    private Long answer_id;   //why not?
    private String prob_text;
    private String prob_attr;
    private String prob_level;
    private String prob_type;
    private String prob_diff;

    private Long lesson_id;
    private Long grammar_id;
    private Long point_id;
    private Long blank_num;

    //add
    private String image_url;
    private String audio_url;

    private int is_delete; //判断是否被删除，默认为0

    public Problem() {
    }

    ;

    public Problem(String prob_text, String prob_attr, String prob_level, String prob_diff, Long lesson_id, Long grammar_id, Long point_id, Long blank_num) {
        this.prob_text = prob_text;
        this.prob_attr = prob_attr;
        this.prob_level = prob_level;
        this.prob_diff = prob_diff;
        this.lesson_id = lesson_id;
        this.grammar_id = grammar_id;
        this.point_id = point_id;
        this.blank_num = blank_num;
    }

    public Long getProb_id() {
        return prob_id;
    }

    public void setProb_id(Long prob_id) {
        this.prob_id = prob_id;
    }

    public Long getOptions_id() {
        return options_id;
    }

    public void setOptions_id(Long options_id) {
        this.options_id = options_id;
    }

    public Long getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Long answer_id) {
        this.answer_id = answer_id;
    }

    public String getProb_text() {
        return prob_text;
    }

    public void setProb_text(String prob_text) {
        this.prob_text = prob_text;
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

    public Long getGrammar_id() {
        return grammar_id;
    }

    public void setGrammar_id(Long grammar_id) {
        this.grammar_id = grammar_id;
    }

    public Long getPoint_id() {
        return point_id;
    }

    public void setPoint_id(Long point_id) {
        this.point_id = point_id;
    }

    public Long getBlank_num() {
        return blank_num;
    }

    public void setBlank_num(Long blank_num) {
        this.blank_num = blank_num;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getProb_type() {
        return prob_type;
    }

    public void setProb_type(String prob_type) {
        this.prob_type = prob_type;
    }
//


    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "prob_id=" + prob_id +
                '}';
    }
}
