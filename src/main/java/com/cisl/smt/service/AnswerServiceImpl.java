package com.cisl.smt.service;

import com.cisl.smt.dao.AnswerRepository;
import com.cisl.smt.po.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService{
    /**
     * @description: 答案数据增删改查实体类
     * @author: Hopenx
     * @date: 2020-11-14 17:13
     */

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Answer getAnswer(Long id){
        return answerRepository.getAnswer(id);
    }

}
