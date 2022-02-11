package com.cisl.smt.service;

import com.cisl.smt.po.Options;

public interface OptionsService {
    Options getOptions(Long id);

    void updateOptions(Long id, String optionA, String optionB, String optionC, String optionD);
}
