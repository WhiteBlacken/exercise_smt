package com.cisl.smt.service;

import com.cisl.smt.dao.ExerciseEvalRepository;
import com.cisl.smt.po.ExerciseEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseEvalServiceImpl implements ExerciseEvalService{
    /**
     * @description: 插入一条评估记录
     * @author: Hopenx
     * @date: 2020-11-27 04:58
     */

    @Autowired
    private ExerciseEvalRepository exerciseEvalRepository;

    @Override
    public void insertExerciseEval(ExerciseEvaluation exerciseEvaluation) {
        exerciseEvalRepository.insertExerciseEval(exerciseEvaluation.getExercise_id(), exerciseEvaluation.getConsume_time(),
                exerciseEvaluation.getExer_eval_score(), exerciseEvaluation.getExer_eval_time(),
                exerciseEvaluation.getExercise_id(), exerciseEvaluation.getUser_id(),
                exerciseEvaluation.getExer_level());
    }

    @Override
    public List<ExerciseEvaluation> getExerciseEval(Long user_id){
        return exerciseEvalRepository.getExerciseEval(user_id);
    }

}
