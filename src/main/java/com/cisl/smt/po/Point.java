package com.cisl.smt.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_point")
public class Point implements Serializable {

    @Id
    @GeneratedValue
    private Long point_id;

    private Long grammar_id;
    private String point_text;

    public Point() {};

    public Long getPoint_id() {
        return point_id;
    }

    public Long getGrammar_id() {
        return grammar_id;
    }

    public String getPoint_text() {
        return point_text;
    }

    public void setPoint_id(Long point_id) {
        this.point_id = point_id;
    }

    public void setGrammar_id(Long grammar_id) {
        this.grammar_id = grammar_id;
    }

    public void setPoint_text(String point_text) {
        this.point_text = point_text;
    }

    @Override
    public String toString() {
        return "Point{" +
                "point_id=" + point_id +
                ", grammar_id=" + grammar_id +
                ", point_text='" + point_text + '\'' +
                '}';
    }
}
