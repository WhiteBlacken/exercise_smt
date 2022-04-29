package com.cisl.smt.service;

import com.cisl.smt.po.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author qxy
 * @Date 2022/4/26 22:10
 * @Version 1.0
 */

public interface LessonService {
    List<Lesson> getByLevelNum(int level_num);

    List<Integer> getFinishedByUserId(Long user_id);

    int getFinishedMaxNumByUserId(Long user_id);

    int getById(int max_lesson_id);

    void recordExercise(Long user_id, int level, int lesson);
}
