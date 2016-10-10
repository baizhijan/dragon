package com.bzj.dragon.service.account;

import com.bzj.dragon.dao.bean.User;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:11:01
 */
public interface UserService {

    /**
     * 创建用户
     * @param user
     * @return
     */
    User saveUser(User user);
}
