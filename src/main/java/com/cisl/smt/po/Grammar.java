package com.cisl.smt.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_grammar")
public class Grammar implements Serializable {

    @Id
    @GeneratedValue
    private Long grammar_id;

    @Column(name = "grammar_text", nullable = false)
    private String grammar_text;

    public Grammar() {}

    public Grammar(String grammar_text) {
        this.grammar_text = grammar_text;
    }

    public Long getGrammar_id() {
        return grammar_id;
    }

    public String getGrammar_text() {
        return grammar_text;
    }

    public void setGrammar_id(Long grammar_id) {
        this.grammar_id = grammar_id;
    }

    public void setGrammar_text(String grammar_text) {
        this.grammar_text = grammar_text;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "grammar_id=" + grammar_id +
                ", grammar_text='" + grammar_text + '\'' +
                '}';
    }
}



