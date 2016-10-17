package com.bzj.dragon.utils.net;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Author: baizhijian
 * Date: 15/11/4 18:08
 */
public class HttpResponseModel implements Serializable {
	private static final String DEFAULT_CHARSET = "UTF-8";

	private boolean hasException = false;
	private int statusCode;
	private String contentType;
	private String charset;
	private byte[] bodyBytes;
	private String exception;
	private String exceptionStackTrace;

	public HttpResponseModel(String exception, String exceptionStackTrace) {
		this.hasException = true;
		this.exception = exception;
		this.exceptionStackTrace = exceptionStackTrace;
	}

	public HttpResponseModel(int statusCode, String contentType, String charset, byte[] bodyBytes) {
		this.statusCode = statusCode;
		this.contentType = contentType;
		this.charset = StringUtils.trimToEmpty(charset);
		if (this.charset.length() == 0) {
			this.charset = DEFAULT_CHARSET;
		}
		this.bodyBytes = bodyBytes;
	}

	public boolean isOk() {
		return !hasException && (statusCode == HttpStatus.SC_OK);
	}

	public boolean isHasException() {
		return hasException;
	}

	public String getException() {
		return exception;
	}

	public String getExceptionStackTrace() {
		return exceptionStackTrace;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getContentType() {
		return contentType;
	}

	public String getCharset() {
		return charset;
	}

	public byte[] getBodyBytes() {
		return bodyBytes;
	}

	public String getBodyString() {
		if (bodyBytes == null || bodyBytes.length == 0) {
			return "";
		}
		try {
			return new String(bodyBytes, charset);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
