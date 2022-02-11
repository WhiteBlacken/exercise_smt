package com.cisl.smt.dao;

import com.cisl.smt.po.ProblemEvaluation;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface ProblemEvalRepository extends JpaRepository<ProblemEvaluation, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into t_prob_eval(prob_eval_id,prob_id,user_id,choice,choice_text,prob_text,analysis,ans,point,type,prob_eval_res,prob_eval_time,consume_time) values(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13)", nativeQuery = true)
    void insertProblemEval(@Param("prob_eval_id") Long prob_eval_id,
                           @Param("prob_id") Long prob_id,
                           @Param("user_id") Long user_id,
                           @Param("choice") String choice,
                           @Param("choice_text") String choice_text,
                           @Param("prob_text") String prob_text,
                           @Param("analysis") String analysis,
                           @Param("ans") String ans,
                           @Param("point") String point,
                           @Param("type") String type,
                           @Param("prob_eval_res") int prob_eval_res,
                           @Param("prob_eval_time") String prob_eval_time,
                           @Param("consume_time") Long consume_time);

    @Query(value = "select * from t_prob_eval where user_id=?1", nativeQuery = true)
    ArrayList<ProblemEvaluation> getProblemEvalByUser(@Param("user_id") Long user_id);

    @Query(value = "select * from t_prob_eval where prob_eval_id=?1 limit 1", nativeQuery = true)
    ProblemEvaluation getProblemEvalById(@Param("prob_eval_id") Long prob_eval_id);

    // 某学生过去两周的刷题简单题数量
    @Query(value = "SELECT COUNT(*) FROM t_prob_eval PE join t_problem P ON PE.prob_id=P.prob_id\n" +
            "WHERE TO_DAYS(NOW()) - TO_DAYS(prob_eval_time) <= 14 AND P.prob_diff='Easy' AND user_id=?1", nativeQuery = true)
    Integer getExerciseEasyNumWeekly(@Param("user_id") Long user_id);

    // 某学生过去两周的刷题中等题数量
    @Query(value = "SELECT COUNT(*) FROM t_prob_eval PE join t_problem P ON PE.prob_id=P.prob_id\n" +
            "WHERE TO_DAYS(NOW()) - TO_DAYS(prob_eval_time) <= 14 AND P.prob_diff='Medium' AND user_id=?1", nativeQuery = true)
    Integer getExerciseMediumNumWeekly(@Param("user_id") Long user_id);

    // 某学生过去两周的刷题困难题数量
    @Query(value = "SELECT COUNT(*) FROM t_prob_eval PE join t_problem P ON PE.prob_id=P.prob_id\n" +
            "WHERE TO_DAYS(NOW()) - TO_DAYS(prob_eval_time) <= 14 AND P.prob_diff='Hard' AND user_id=?1", nativeQuery = true)
    Integer getExerciseHardNumWeekly(@Param("user_id") Long user_id);

    // 某学生掌握考点 TOP3
    @Query(value = "SELECT r.point FROM (\n" +
            "\tSELECT point,count(*) as right_cnt FROM t_prob_eval WHERE point!='未知考点' AND prob_eval_res=1 AND user_id=?1 GROUP BY point) r\n" +
            "\tLEFT OUTER JOIN \n" +
            "\t(SELECT point,count(*) as wrong_cnt FROM t_prob_eval WHERE point!='未知考点' AND prob_eval_res=0 AND user_id=?1 GROUP BY point) w\n" +
            "\tON r.point=w.point\n" +
            "\tWHERE r.right_cnt/(r.right_cnt+w.wrong_cnt) >= 0.6\n" +
            "\tORDER BY r.right_cnt/(r.right_cnt+w.wrong_cnt) DESC\n" +
            "\tLIMIT 3", nativeQuery = true)
    String[] getExerciseRightPoint(@Param("user_id") Long user_id);

    // 某学生未掌握考点 TOP3
    @Query(value = "SELECT r.point FROM (\n" +
            "\tSELECT point,count(*) as right_cnt FROM t_prob_eval WHERE point!='未知考点' AND prob_eval_res=1 AND user_id=?1 GROUP BY point) r\n" +
            "\tLEFT OUTER JOIN \n" +
            "\t(SELECT point,count(*) as wrong_cnt FROM t_prob_eval WHERE point!='未知考点' AND prob_eval_res=0 AND user_id=?1 GROUP BY point) w\n" +
            "\tON r.point=w.point\n" +
            "\tWHERE r.right_cnt/(r.right_cnt+w.wrong_cnt) >= 0 AND r.right_cnt/(r.right_cnt+w.wrong_cnt) < 0.6\n" +
            "\tORDER BY r.right_cnt/(r.right_cnt+w.wrong_cnt)\n" +
            "\tLIMIT 3", nativeQuery = true)
    String[] getExerciseWrongPoint(@Param("user_id") Long user_id);

    // 某学生已掌握考点个数
    @Query(value = "SELECT  COUNT(*) FROM (\n" +
            "\tSELECT point,count(*) as right_cnt FROM t_prob_eval WHERE point!='未知考点' AND prob_eval_res=1 AND user_id=?1 GROUP BY point) r\n" +
            "\tLEFT OUTER JOIN \n" +
            "\t(SELECT point,count(*) as wrong_cnt FROM t_prob_eval WHERE point!='未知考点' AND prob_eval_res=0 AND user_id=?1 GROUP BY point) w\n" +
            "\tON r.point=w.point\n" +
            "\tWHERE r.right_cnt/(r.right_cnt+w.wrong_cnt) >= 0.6", nativeQuery = true)
    Integer getHandlePointCount(@Param("user_id") Long user_id);
}
