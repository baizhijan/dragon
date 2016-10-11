package com.bzj.dragon.test;


import com.bzj.dragon.utils.mail.MailManager;
import com.bzj.dragon.utils.mail.bean.Email;
import com.bzj.dragon.utils.mail.bean.ServerConfig;
import com.bzj.dragon.utils.mail.enums.ContentType;
import junit.framework.TestCase;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-11
 * Time:14:21
 */
public class MailTest{

    public static void main(String[] args) {
        ServerConfig config = new ServerConfig();
        config.setSmtp("smtp.163.com");
        config.setAuth(true);
        config.setUsername("mailName");
        config.setPassword("password");

        Email email = new Email();
        email.setSender("mailName@163.com");
        String[] arr = {"12345678@qq.com"};
        email.setAddressees(arr);
        email.setCc(arr);
        email.setBcc(arr);
        String[] affix = {"D:\\myExcel.xlsx"};
        email.setAffixName(affix);
        email.setContentType(ContentType.TEXT);
        email.setContent(" 纯文本邮件发送 + 附件添加");
        email.setSubject("测试邮件");

        MailManager mailManager = new MailManager(config);
        mailManager.sendNew(email);
    }
}
