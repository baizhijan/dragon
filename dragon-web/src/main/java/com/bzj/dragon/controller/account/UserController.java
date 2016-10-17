package com.bzj.dragon.controller.account;

import com.bzj.dragon.controller.account.vo.UserVo;
import com.bzj.dragon.dao.bean.User;
import com.bzj.dragon.service.account.UserService;
import com.bzj.dragon.utils.common.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户管理
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:10:51
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建用户
     *
     * @param user
     * @return UserVo
     */
    @ResponseBody
    @RequestMapping("/save")
    public UserVo creatUser(UserVo user) {
        User saveUser = userService.saveUser(BeanMapper.map(user, User.class));
        return BeanMapper.map(saveUser,UserVo.class);
    }

}