package com.example.project.pojo;


import java.io.Serializable;

/**
 * @Author: yangwenchuan
 * @Date: 2022/11/14 22:29
 * Discretion:
 */

public class Kind implements Serializable {

    private int kid;
    private String content;

    public Kind(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Kind{" +
                "kid=" + kid +
                ", content='" + content + '\'' +
                '}';
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Kind(int kid, String content) {
        this.kid = kid;
        this.content = content;
    }

    public Kind() {
    }
}

