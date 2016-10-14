package com.bzj.dragon.mongodb.bean;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-14
 * Time:14:34
 */
public class User{

    private int id;
    private String name;
    private String avatar;
    private String nickName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
