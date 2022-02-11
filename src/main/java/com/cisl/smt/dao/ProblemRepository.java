package com.cisl.smt.dao;

import com.cisl.smt.po.Problem;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {  //Long表示主键的类型

    @Query(value = "select * from t_problem where prob_id=:num", nativeQuery = true)
    Problem getProblemByProb_id(@Param("num") Long num);

    @Query(value = "select * from t_problem where lesson_id=:lesson_id and prob_level=:prob_level", nativeQuery = true)
    List<Problem> getProblemByLevelAndLesson_id(@Param("prob_level") String prob_level, @Param("lesson_id") Long lesson_id);

    @Query(value = "select * from t_problem where point_id=:point_id", nativeQuery = true)
    List<Problem> getProblemByPoint_id(@Param("point_id") Long point_id);

    @Query(value = "select * from t_problem", nativeQuery = true)
    List<Problem> getAllProblem();

    @Query(value = "SELECT prob_id FROM t_problem ORDER BY prob_id DESC LIMIT 1", nativeQuery = true)
    Long getLastProb_id();

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_problem SET prob_text=:prob_text, prob_attr=:prob_attr, prob_diff=:prob_diff, prob_level=:prob_level, blank_num=:blank_num, point_id=:point_id, lesson_id=:lesson_id WHERE prob_id=:prob_id", nativeQuery = true)
    void updateProblemByPoint_id(@Param("prob_id") Long prob_id,
                                 @Param("prob_text") String prob_text,
                                 @Param("prob_attr") String prob_attr,
                                 @Param("prob_diff") String prob_diff,
                                 @Param("prob_level") String prob_level,
                                 @Param("lesson_id") Long lesson_id,
                                 @Param("point_id") Long point_id,
                                 @Param("blank_num") Long blank_num);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO t_problem(prob_id, grammar_id, point_id, prob_attr, prob_diff, prob_level, prob_text, options_id, answer_id, lesson_id, blank_num) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11);", nativeQuery = true)
    void insertProblem(Long prob_id, Long grammar_id, Long point_id, String prob_attr, String prob_diff, String prob_level, String prob_text, Long options_id, Long answer_id, Long lesson_id, Long blank_num);

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_problem SET prob_text=:prob_text WHERE prob_id=:prob_id", nativeQuery = true)
    void markDeleteProblem(@Param("prob_id") Long prob_id, @Param("prob_text") String prob_text);

}
