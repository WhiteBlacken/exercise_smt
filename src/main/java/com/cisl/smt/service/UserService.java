package com.cisl.smt.service;

import com.cisl.smt.po.User;

public interface UserService {
    User saveUser(User user);

    User getUserByUsername(String username);
}
