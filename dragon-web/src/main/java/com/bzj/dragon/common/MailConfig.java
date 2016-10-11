package com.bzj.dragon.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:16:36
 */
public class MailConfig {

    @Value("#{mail['smtp']}")
    private String smtp;
    @Value("#{mail['auth']}")
    private boolean auth;
    @Value("#{mail['userName']}")
    private String userName;
    @Value("#{mail['password']}")
    private String password;

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
