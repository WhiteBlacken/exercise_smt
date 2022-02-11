package com.cisl.smt.service;

import com.cisl.smt.po.ReviewCollection;


public interface ReviewCollecService {
    ReviewCollection getCollec(Long user_id);

    void insertCollec(Long user_id, String prob_eval_list, String prob_list);

    void updateCollec(Long user_id, String prob_eval_list, String prob_list);
}
