package com.anyihao.ayb.bean;

import java.io.Serializable;

/**
 * Created by Admin on 2017/7/26.
 */

public class JPushBean implements Serializable {


    /**
     * type : url
     * content : http://www.baidu.com
     * title : 活动页面
     */

    private String type;
    private String content;
    private String title;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
