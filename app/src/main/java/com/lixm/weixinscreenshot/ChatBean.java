package com.lixm.weixinscreenshot;

/**
 * Describe:
 * <p>
 * Author: Lixm
 * Date: 2019/7/19
 */
public class ChatBean {
    private int type;//0 左； 1 右
    private String content;
    private int contentType;//0 文字内容；1图片内容
    private String headPath;
    private String picPath;

    public ChatBean() {
    }

    public ChatBean(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
