package com.bzj.dragon.utils.mail.bean;

import com.bzj.dragon.utils.mail.enums.ContentType;

import java.io.Serializable;

/**
 * 邮件类
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:16:12
 */
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 发件人 **/
	private String sender;
	/** 收件人 **/
	private String[] addressees;
	/** 抄送人 **/
	private String[] cc;
	/** 暗送人 **/
	private String[] bcc;
	/** 邮件主题 **/
	private String subject;
	/** 邮件内容**/
	private String content;
	/** 文本内容类型 **/
	private ContentType contentType = ContentType.TEXT;
	/** 附件 **/
	private boolean affix;
	/** 附件地址 **/
	private String[] affixName;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String[] getAddressees() {
		return addressees;
	}

	public void setAddressees(String[] addressees) {
		this.addressees = addressees;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public boolean hasAffix() {
		return affix;
	}

	public String[] getAffixName() {
		return affixName;
	}

	public void setAffixName(String[] affixName) {
		this.affixName = affixName;
		affix = (this.affixName != null && this.affixName.length > 0);
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
}
