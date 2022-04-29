package com.cisl.smt.service;

import com.cisl.smt.dao.LessonRepository;
import com.cisl.smt.dao.StuLessonRecordRepository;
import com.cisl.smt.po.Lesson;
import com.cisl.smt.po.StuLessonRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author qxy
 * @Date 2022/4/26 22:11
 * @Version 1.0
 */
@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StuLessonRecordRepository stuLessonRecordRepository;

    @Override
    public List<Lesson> getByLevelNum(int level_num) {
        return lessonRepository.findAllByLessonNum(level_num);
    }

    @Override
    public List<Integer> getFinishedByUserId(Long user_id) {
        return stuLessonRecordRepository.getFinishedByUserId(user_id);
    }

    @Override
    public int getFinishedMaxNumByUserId(Long user_id) {
        try{
            return stuLessonRecordRepository.getFinishedMaxNumByUserId(user_id);
        }catch (Exception e){
            return 0;
        }


    }

    @Override
    public int getById(int max_lesson_id) {
        try{
            return lessonRepository.getOne(max_lesson_id).getLesson_num();
        }catch(Exception e){
            return 0;
        }

    }

    @Override
    public void recordExercise(Long user_id, int level, int lesson) {
        int lesson_id = lessonRepository.getIdByLevelAndLesson(level,lesson);
        StuLessonRecord record = new StuLessonRecord(user_id,lesson_id);
        stuLessonRecordRepository.save(record);
    }
}
