package com.cisl.smt.service;

import com.cisl.smt.dao.OptionsRepository;
import com.cisl.smt.po.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionsServiceImpl implements OptionsService{
    /**
     * @description: 获取选项组
     * @author: Hopenx
     * @date: 2020-10-19 21:07
     */

    @Autowired
    private OptionsRepository optionsRepository;

    @Override
    public Options getOptions(Long id){
        return optionsRepository.getOptions(id);
    }

    @Override
    public void updateOptions(Long id, String optionA, String optionB, String optionC, String optionD){
        optionsRepository.updateOptions(id, optionA, optionB, optionC, optionD);
    }

}
