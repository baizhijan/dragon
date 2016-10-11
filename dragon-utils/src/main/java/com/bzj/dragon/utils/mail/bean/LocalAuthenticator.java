package com.bzj.dragon.utils.mail.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-11
 * Time:11:09
 */
public class LocalAuthenticator extends Authenticator {
    private String username;
    private String password;

    public LocalAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        // TODO Auto-generated method stub
        return new PasswordAuthentication(username, password);
    }
}
