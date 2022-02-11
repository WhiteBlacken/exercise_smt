package com.cisl.smt.service;

import com.cisl.smt.dao.UserRepository;
import com.cisl.smt.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    /**
     * @description: 实现用户操作类
     * @author: Hopenx
     * @date: 2020-10-23 17:21
     */

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


}
