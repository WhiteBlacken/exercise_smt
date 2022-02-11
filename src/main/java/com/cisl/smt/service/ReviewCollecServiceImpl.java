package com.cisl.smt.service;

import com.cisl.smt.dao.ReviewCollecRepository;
import com.cisl.smt.po.ReviewCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewCollecServiceImpl implements ReviewCollecService{
    /**
     * @description: 错题集的查询与添加类
     * @author: Hopenx
     * @date: 2020-11-27 07:52
     */

    @Autowired
    private ReviewCollecRepository reviewCollecRepository;

    @Override
    public ReviewCollection getCollec(Long user_id){
        return reviewCollecRepository.getCollec(user_id);
    }

    @Override
    public void insertCollec(Long user_id, String prob_eval_list, String prob_list){
        reviewCollecRepository.insertCollec(user_id, prob_eval_list, prob_list);
    }

    @Override
    public void updateCollec(Long user_id, String prob_eval_list, String prob_list){
        reviewCollecRepository.updateCollec(user_id, prob_eval_list, prob_list);
    }
}
