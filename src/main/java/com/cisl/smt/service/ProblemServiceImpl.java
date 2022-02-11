package com.cisl.smt.service;

import com.cisl.smt.dao.ProblemRepository;
import com.cisl.smt.po.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    /**
     * @description: 实现对题目的获取等操作
     * @author: Hopenx
     * @date: 2020-10-19 20:02
     */

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public Problem getProblemByProb_id(Long num) {
        return problemRepository.getProblemByProb_id(num);
    }

    @Override
    public List<Problem> getProblemByLevelAndLesson_id(String prob_level, Long lesson_id) {
        return problemRepository.getProblemByLevelAndLesson_id(prob_level, lesson_id);
    }

    @Override
    public List<Problem> getProblemByPoint_id(Long point_id) {
        return problemRepository.getProblemByPoint_id(point_id);
    }

    @Override
    public List<Problem> getAllProblem() {
        return problemRepository.getAllProblem();
    }

    @Override
    public Long getLastProb_id() {
        return problemRepository.getLastProb_id();
    }

    @Override
    public void updateProblemByPoint_id(Long prob_id, String prob_text, String prob_attr, String prob_diff, String prob_level, Long lesson_id, Long point_id, Long blank_num) {
        problemRepository.updateProblemByPoint_id(prob_id, prob_text, prob_attr, prob_diff, prob_level, lesson_id, point_id, blank_num);
    }

    @Override
    public void insertProblem(Long prob_id, Long grammar_id, Long point_id, String prob_attr, String prob_diff, String prob_level, String prob_text, Long options_id, Long answer_id, Long lesson_id, Long blank_num) {
        problemRepository.insertProblem(prob_id, grammar_id, point_id, prob_attr, prob_diff, prob_level, prob_text, options_id, answer_id, lesson_id, blank_num);
    }

}
