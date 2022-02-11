package com.cisl.smt.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_point_master")
public class PointMaster {

    @Id
    @GeneratedValue
    private Long point_master_id;
    private Long point_id;
    private Long user_id;
    private Long master_level;

    public PointMaster() {};

    public Long getPoint_master_id() {
        return point_master_id;
    }

    public void setPoint_master_id(Long point_master_id) {
        this.point_master_id = point_master_id;
    }

    public Long getPoint_id() {
        return point_id;
    }

    public void setPoint_id(Long point_id) {
        this.point_id = point_id;
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
        return "PointMaster{" +
                "point_master_id=" + point_master_id +
                ", point_id=" + point_id +
                ", user_id=" + user_id +
                ", master_level=" + master_level +
                '}';
    }
}
