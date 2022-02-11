package com.cisl.smt.po;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_answer")
public class Answer {

    @Id
    @GeneratedValue
    private Long answer_id;

    private String answer_text;
    private String analysis_text;

    public Answer() {};

    public Answer(String answer_text, String analysis_text) {
        this.answer_text = answer_text;
        this.analysis_text = analysis_text;
    }

    public Long getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Long answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public String getAnalysis_text() {
        return analysis_text;
    }

    public void setAnalysis_text(String analysis_text) {
        this.analysis_text = analysis_text;
    }
}
