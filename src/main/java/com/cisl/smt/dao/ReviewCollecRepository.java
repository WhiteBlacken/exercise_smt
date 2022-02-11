package com.cisl.smt.dao;

import com.cisl.smt.po.ReviewCollection;
//import org.apache.ibatis.annotations.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewCollecRepository extends JpaRepository<ReviewCollection, Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into t_rev_collec(user_id, prob_eval_list, prob_list) values(?1, ?2, ?3)", nativeQuery = true)
    void insertCollec(@Param("user_id") Long user_id,
                      @Param("prob_eval_list") String prob_eval_list,
                      @Param("prob_list") String prob_list);

    @Query(value = "select * from t_rev_collec where user_id=?1 limit 1", nativeQuery = true)
    ReviewCollection getCollec(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "update t_rev_collec t set t.prob_eval_list=?2, t.prob_list=?3 where t.user_id=?1", nativeQuery = true)
    void updateCollec(@Param("user_id") Long user_id,
                      @Param("prob_eval_list") String prob_eval_list,
                      @Param("prob_list") String prob_list);
}
