package com.bzj.dragon.mongodb.dao;

import com.bzj.dragon.mongodb.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-14
 * Time:14:42
 */
@Component
public class UserDao extends BaseMongoDAOImpl<User>{

    @Autowired
    @Override
    protected void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
