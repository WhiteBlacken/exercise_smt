package com.cisl.smt.dao;

import com.cisl.smt.po.StuLessonRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author qxy
 * @Date 2022/4/26 22:27
 * @Version 1.0
 */
public interface StuLessonRecordRepository extends JpaRepository<StuLessonRecord,Long> {
    @Query(value = "SELECT DISTINCT lesson_id from t_stu_lesson WHERE user_id = ?1",nativeQuery = true)
    List<Integer> getFinishedByUserId(Long user_id);

    @Query(value = "SELECT max(lesson_id) from t_stu_lesson WHERE user_id = ?1",nativeQuery = true)
    int getFinishedMaxNumByUserId(Long user_id);
}
