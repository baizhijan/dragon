package com.bzj.dragon.utils.mail.bean;

import java.io.Serializable;

/**
 * 邮件发送配置器
 * User:aaronbai@tcl.com
 * Date:2016-10-10
 * Time:16:12
 */
public class ServerConfig implements Serializable {

	private String smtp;
	private boolean auth;
	private String username;
	private String password;
	private boolean proxy;
	private String proxyHost;
	private int proxyPort = 80;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isProxy() {
		return proxy;
	}

	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}