package com.bzj.dragon.service.account.impl;

import com.bzj.dragon.dao.UserDao;
import com.bzj.dragon.dao.bean.User;
import com.bzj.dragon.service.account.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:11:02
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User saveUser(User user) {
        userDao.saveUser(user);
        return user;
    }
}
