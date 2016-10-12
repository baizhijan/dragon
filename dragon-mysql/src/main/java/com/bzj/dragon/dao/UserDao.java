package com.bzj.dragon.dao;

import com.bzj.dragon.dao.bean.User;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:11:04
 */
public interface UserDao {

    /**
     * 创建用户
     * @param user
     * @return
     */
    int saveUser(User user);
}
