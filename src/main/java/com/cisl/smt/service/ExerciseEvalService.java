package com.cisl.smt.service;

import com.cisl.smt.po.ExerciseEvaluation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExerciseEvalService {
    void insertExerciseEval(ExerciseEvaluation exerciseEvaluation);

    List<ExerciseEvaluation> getExerciseEval(Long user_id);
}
