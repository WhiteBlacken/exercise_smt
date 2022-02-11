package com.cisl.smt.dao;

import com.cisl.smt.po.Answer;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query(value = "select * from t_answer where answer_id=:id", nativeQuery = true)
    Answer getAnswer(@Param("id") Long id);

    @Query(value = "SELECT answer_id FROM t_answer ORDER BY answer_id DESC LIMIT 1", nativeQuery = true)
    Long getLastAnswer_id();

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_answer SET answer_text=:answer_text, analysis_text=:analysis WHERE answer_id=:id", nativeQuery=true)
    void updateAnswer(@Param("id") Long id, @Param("analysis") String analysis, @Param("answer_text") String answer_text);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO t_answer(answer_id, analysis_text, answer_text) VALUES (?1, ?2, ?3);", nativeQuery=true)
    void insertAnswer(Long id, String analysis, String answer_text);
}
