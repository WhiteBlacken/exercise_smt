package com.cisl.smt.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_grammar_master")
public class GrammarMaster {

    @Id
    @GeneratedValue
    private Long grammar_master_id;
    private Long grammar_id;
    private Long user_id;
    private Long master_level;

    public GrammarMaster() {};

    public Long getGrammar_master_id() {
        return grammar_master_id;
    }

    public void setGrammar_master_id(Long grammar_master_id) {
        this.grammar_master_id = grammar_master_id;
    }

    public Long getGrammar_id() {
        return grammar_id;
    }

    public void setGrammar_id(Long grammar_id) {
        this.grammar_id = grammar_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getMaster_level() {
        return master_level;
    }

    public void setMaster_level(Long master_level) {
        this.master_level = master_level;
    }

    @Override
    public String toString() {
        return "GrammarMaster{" +
                "grammar_master_id=" + grammar_master_id +
                ", grammar_id=" + grammar_id +
                ", user_id=" + user_id +
                ", master_level=" + master_level +
                '}';
    }
}
