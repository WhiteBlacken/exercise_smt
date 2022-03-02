package com.cisl.smt.po;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_options")
public class Options {

    @Id
    @GeneratedValue
    private Long options_id;

    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;

    //add
    private String a_image_url;
    private String b_image_url;
    private String c_image_url;
    private String d_image_url;
    private String a_audio_url;
    private String b_audio_url;
    private String c_audio_url;
    private String d_audio_url;

    private String choice_type;

    public Options() {
    }

    ;

    public Options(String option_a, String option_b, String option_c, String option_d) {
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
    }

    public Options(String option_a, String option_b, String option_c, String option_d, String a_image_url, String b_image_url, String c_image_url, String d_image_url, String a_audio_url, String b_audio_url, String c_audio_url, String d_audio_url, String choice_type) {
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.a_image_url = a_image_url;
        this.b_image_url = b_image_url;
        this.c_image_url = c_image_url;
        this.d_image_url = d_image_url;
        this.a_audio_url = a_audio_url;
        this.b_audio_url = b_audio_url;
        this.c_audio_url = c_audio_url;
        this.d_audio_url = d_audio_url;
        this.choice_type = choice_type;
    }

    public Long getOptions_id() {
        return options_id;
    }

    public void setOptions_id(Long options_id) {
        this.options_id = options_id;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getA_image_url() {
        return a_image_url;
    }

    public void setA_image_url(String a_image_url) {
        this.a_image_url = a_image_url;
    }

    public String getB_image_url() {
        return b_image_url;
    }

    public void setB_image_url(String b_image_url) {
        this.b_image_url = b_image_url;
    }

    public String getC_image_url() {
        return c_image_url;
    }

    public void setC_image_url(String c_image_url) {
        this.c_image_url = c_image_url;
    }

    public String getD_image_url() {
        return d_image_url;
    }

    public void setD_image_url(String d_image_url) {
        this.d_image_url = d_image_url;
    }

    public String getA_audio_url() {
        return a_audio_url;
    }

    public void setA_audio_url(String a_audio_url) {
        this.a_audio_url = a_audio_url;
    }

    public String getB_audio_url() {
        return b_audio_url;
    }

    public void setB_audio_url(String b_audio_url) {
        this.b_audio_url = b_audio_url;
    }

    public String getC_audio_url() {
        return c_audio_url;
    }

    public void setC_audio_url(String c_audio_url) {
        this.c_audio_url = c_audio_url;
    }

    public String getD_audio_url() {
        return d_audio_url;
    }

    public void setD_audio_url(String d_audio_url) {
        this.d_audio_url = d_audio_url;
    }

    public String getChoice_type() {
        return choice_type;
    }

    public void setChoice_type(String choice_type) {
        this.choice_type = choice_type;
    }
}
