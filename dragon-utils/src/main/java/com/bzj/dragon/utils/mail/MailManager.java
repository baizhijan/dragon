package com.bzj.dragon.utils.mail;

import com.bzj.dragon.utils.common.CollectionUtil;
import com.bzj.dragon.utils.mail.bean.LocalAuthenticator;
import com.bzj.dragon.utils.mail.bean.ServerConfig;
import com.bzj.dragon.utils.mail.bean.Email;
import com.bzj.dragon.utils.mail.enums.ContentType;
import com.bzj.dragon.utils.mail.exception.MailException;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.CaseInsensitiveMap;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件服务类
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:16:12
 */
public class MailManager {

    private Session session;

    /**
     * 通过够着函数初始化session
     * @param config
     */
    public MailManager(ServerConfig config) {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getSmtp());
        props.put("mail.smtp.auth", config.isAuth());
        Authenticator authenticator = null;
        if (config.isAuth()) {
            authenticator = new LocalAuthenticator(config.getUsername(), config.getPassword());
        }
        if (config.isProxy()) {
            System.setProperty("http.proxyHost", config.getProxyHost());
            System.setProperty("http.proxyPort", String.valueOf(config.getProxyPort()));
        }
        session = Session.getDefaultInstance(props, authenticator);
    }

    /**
     * 发送邮件
     * @param email
     * @return
     */
    public boolean sendEmail(Email email) {
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(email.getSender()));
            msg.setSubject(email.getSubject());
            msg.setSentDate(new Date());

            String[] addressees = email.getAddressees();
            if (!CollectionUtil.isEmpty(addressees)) {
                InternetAddress[] toInternetAddressees = creatInternetAddress(addressees);
                msg.setRecipients(RecipientType.TO, toInternetAddressees);
            }

            //添加抄送人
            String[] cc = email.getCc();
            if (!CollectionUtil.isEmpty(cc)) {
                InternetAddress[] ccInternetAddressees = creatInternetAddress(cc);
                msg.setRecipients(RecipientType.CC, ccInternetAddressees);
            }

            //添加暗送人
            String[] bcc = email.getBcc();
            if (!CollectionUtil.isEmpty(bcc)) {
                InternetAddress[] bccInternetAddressees = creatInternetAddress(bcc);
                msg.setRecipients(RecipientType.BCC, bccInternetAddressees);
            }

            Multipart multipart = addAffix(email);
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new MailException("sendMail is exception", e);
        } catch (IOException e) {
            throw new MailException("affix file is notFindException", e);
        }
        return true;
    }

    /**
     * 添加附件
     *
     * @param email
     * @throws IOException
     * @throws MessagingException
     */
    public Multipart addAffix(Email email) throws IOException, MessagingException {
        Multipart mp = new MimeMultipart();
        if (email.hasAffix()) {
            String[] affixName = email.getAffixName();
            for (int i = 0; i < affixName.length; i++) {
                MimeBodyPart affix = new MimeBodyPart();
                affix.attachFile(affixName[i]);
                mp.addBodyPart(affix);
            }
        }
        switch (email.getContentType()){
            case HTML:
                BodyPart body = new MimeBodyPart();
                body.setContent(email.getContent(), "text/html;charset=utf-8");
                mp.addBodyPart(body);
                break;
            case TEXT:
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(email.getContent());
                mp.addBodyPart(messageBodyPart);
                break;
        }
        return mp;
    }


    /**
     * 地址数组转地址对象
     *
     * @param addressStr
     * @return
     * @throws AddressException
     */
    public InternetAddress[] creatInternetAddress(String[] addressStr) throws AddressException {
        ArrayList<InternetAddress> addressList = new ArrayList<>();
        for (String address : addressStr) {
            addressList.add(new InternetAddress(address));
        }
        return addressList.toArray(new InternetAddress[addressStr.length]);
    }
}
