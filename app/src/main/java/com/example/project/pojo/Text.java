package com.example.project.pojo;


import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangwenchuan
 * @Date: 2022/11/14 22:29
 * Discretion:
 */

public class Text implements Serializable {


    private int tid; //主键

    private String kind; //类型

    private String theme; //题目

    private String describle; //描述

    private String article; //文章

    private int knums;  //点赞数 热门

    private int cnums;   //收藏数 推荐

    private Date c_date;//创建日期

    private String isAnon;//0为匿名

    //重复无用，但必须和后端一致，没办法
    private String username;//绑定用户名

    private String virname; //呢称

    private Custer custer;//用户  一对一


    @Override
    public String toString() {
        return "Text{" +
                "tid=" + tid +
                ", kind='" + kind + '\'' +
                ", theme='" + theme + '\'' +
                ", describle='" + describle + '\'' +
                ", article='" + article + '\'' +
                ", knums=" + knums +
                ", cnums=" + cnums +
                ", c_date=" + c_date +
                ", isAnon='" + isAnon + '\'' +
                ", username='" + username + '\'' +
                ", virname='" + virname + '\'' +
                '}';
    }

    public Text() {
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getKnums() {
        return knums;
    }

    public void setKnums(int knums) {
        this.knums = knums;
    }

    public int getCnums() {
        return cnums;
    }

    public void setCnums(int cnums) {
        this.cnums = cnums;
    }

    public Date getC_date() {
        return c_date;
    }

    public void setC_date(Date c_date) {
        this.c_date = c_date;
    }

    public String getIsAnon() {
        return isAnon;
    }

    public void setIsAnon(String isAnon) {
        this.isAnon = isAnon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVirname() {
        return virname;
    }

    public void setVirname(String virname) {
        this.virname = virname;
    }

    public Text(int tid, String kind, String theme, String describle, String article, int knums, int cnums, Date c_date, String isAnon, String username, String virname) {
        this.tid = tid;
        this.kind = kind;
        this.theme = theme;
        this.describle = describle;
        this.article = article;
        this.knums = knums;
        this.cnums = cnums;
        this.c_date = c_date;
        this.isAnon = isAnon;
        this.username = username;
        this.virname = virname;
    }
}

