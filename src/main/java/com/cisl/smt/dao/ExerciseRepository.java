package com.cisl.smt.dao;

import com.cisl.smt.po.Exercise;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into t_exercise(exercise_id,exercise_attr,prob_list,grammar_id,update_time) values(?1,?2,?3,?4,?5)", nativeQuery = true)
    void insertExercise(@Param("exercise_id") Long exercise_id,
                        @Param("exercise_attr") String exercise_attr,
                        @Param("prob_list") String prob_list,
                        @Param("grammar_id") Long grammar_id,
                        @Param("update_time") String update_time);
}
