package com.bzj.dragon.utils.net.netEnum;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-17
 * Time:14:50
 */
public enum ContentTypeEnum {

    GENERAL("application/x-www-form-urlencoded"),
    XML("text/xml");

    private String content;
    ContentTypeEnum(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
