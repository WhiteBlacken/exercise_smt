package com.cisl.smt.service;

import com.cisl.smt.po.Problem;

import java.util.List;

public interface ProblemService {
    Problem getProblemByProb_id(Long num);

    List<Problem> getProblemByLevelAndLesson_id(String prob_level, Long lesson_id);

    List<Problem> getProblemByPoint_id(Long point_id);

    List<Problem> getAllProblem();

    Long getLastProb_id();

    void updateProblemByPoint_id(Long prob_id, String prob_text, String prob_attr, String prob_diff, String prob_level, Long lesson_id, Long point_id, Long blank_num);

    void insertProblem(Long prob_id, Long grammar_id, Long point_id, String prob_attr, String prob_diff, String prob_level, String prob_text, Long options_id, Long answer_id, Long lesson_id, Long blank_num);

}
