package com.cisl.smt.service;

import com.cisl.smt.dao.ExerciseRepository;
import com.cisl.smt.po.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService{
    /**
     * @description: 插入一条出卷记录
     * @author: Hopenx
     * @date: 2020-11-26 19:48
     */

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public void insertExercise(Exercise exercise){
        exerciseRepository.insertExercise(exercise.getExercise_id(), exercise.getExercise_attr(), exercise.getProb_list(),
                exercise.getGrammar_id(), exercise.getUpdate_time());
    }

}
