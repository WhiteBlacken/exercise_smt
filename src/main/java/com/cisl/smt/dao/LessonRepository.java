package com.cisl.smt.dao;

import com.cisl.smt.po.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author qxy
 * @Date 2022/4/26 22:14
 * @Version 1.0
 */
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query(value = "SELECT * FROM t_lesson where level_num = ?1", nativeQuery = true)
    List<Lesson> findAllByLessonNum(int level_num);

    @Query(value = "SELECT id FROM t_lesson where level_num = ?1 and lesson_num= ?2", nativeQuery = true)
    int getIdByLevelAndLesson(int level, int lesson);
}
