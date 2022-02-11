package com.cisl.smt.dao;

import com.cisl.smt.po.ExerciseEvaluation;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExerciseEvalRepository extends JpaRepository<ExerciseEvaluation, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into t_exer_eval(exer_eval_id,consume_time,exer_eval_score,exer_eval_time,exercise_id,user_id,exer_level) values(?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
    void insertExerciseEval(@Param("exer_eval_id") Long id,
                            @Param("consume_time") Long consume_time,
                            @Param("exer_eval_score") Long score,
                            @Param("exer_eval_time") String exer_eval_time,
                            @Param("exercise_id") Long exercise_id,
                            @Param("user_id") Long user_id,
                            @Param("exer_level") String exer_level
    );

    @Query(value = "select * from t_exer_eval where user_id=?1", nativeQuery = true)
    List<ExerciseEvaluation> getExerciseEval(@Param("user_id") Long user_id);

    // 某学生过去两周的刷题次数
    @Query(value = "SELECT count(*) FROM t_exer_eval WHERE TO_DAYS(NOW()) - TO_DAYS(exer_eval_time) <= 14 AND user_id=?1", nativeQuery = true)
    Integer getExerciseTimesWeekly(@Param("user_id") Long user_id);

    // 某学生过去两周的刷题平均分
    @Query(value = "SELECT ifnull(avg(exer_eval_score),0) FROM t_exer_eval WHERE TO_DAYS(NOW()) - TO_DAYS(exer_eval_time) <= 14 AND user_id=?1", nativeQuery = true)
    Integer getExerciseAvgScoreWeekly(@Param("user_id") Long user_id);

    // 某学生上一阶段的刷题平均分
    @Query(value = "SELECT ifnull(avg(exer_eval_score),0) FROM t_exer_eval WHERE TO_DAYS(NOW()) - TO_DAYS(exer_eval_time) <= 28 AND TO_DAYS(NOW()) - TO_DAYS(exer_eval_time) > 14 AND user_id=?1", nativeQuery = true)
    Integer getExerciseLastAvgWeekly(@Param("user_id") Long user_id);

    // 某学生所有刷题平均分
    @Query(value = "SELECT avg(exer_eval_score) FROM t_exer_eval WHERE exer_eval_score!=0 AND user_id=?1", nativeQuery = true)
    Integer getExerciseScoreAvg(@Param("user_id") Long user_id);

    // 所有学生刷题平均分
    @Query(value = "SELECT avg(exer_eval_score) FROM t_exer_eval WHERE exer_eval_score!=0", nativeQuery = true)
    Integer getExerciseAllAvg();

    // 某学生所有刷题平均耗时
    @Query(value = "SELECT avg(consume_time) FROM t_exer_eval WHERE exer_eval_score!=0 AND consume_time<2000 AND user_id=?1", nativeQuery = true)
    Integer getExerciseTimeAvg(@Param("user_id") Long user_id);

    // 某学生过去 10 次刷题得分
    @Query(value = "SELECT exer_eval_score FROM t_exer_eval\n" +
            "WHERE exer_eval_score > 5 AND user_id=?1\n" +
            "LIMIT 10", nativeQuery = true)
    int[] getLastTenExercise(@Param("user_id") Long user_id);

}
