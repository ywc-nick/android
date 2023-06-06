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

    private Kind kind; //类型

    private String theme; //题目

    private String article; //文章

    private int knums;  //点赞数 热门

    private int cnums;   //收藏数 推荐

    private String c_date;//创建日期


    private Custer custer;//用户  一对一


    public Text() {
    }

    public Text(String theme, String article, int knums, int cnums) {
        this.theme = theme;
        this.article = article;
        this.knums = knums;
        this.cnums = cnums;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }


    @Override
    public String toString() {
        return "Text{" +
                "tid=" + tid +
                ", kind='" + kind + '\'' +
                ", theme='" + theme + '\'' +

                ", article='" + article + '\'' +
                ", knums=" + knums +
                ", cnums=" + cnums +
                ", c_date=" + c_date +

                ", custer=" + custer +
                '}';
    }

    public Custer getCuster() {
        return custer;
    }

    public void setCuster(Custer custer) {
        this.custer = custer;
    }

    public Text(int tid, Kind kind, String theme, String describle, String article, int knums, int cnums, String c_date,  Custer custer) {
        this.tid = tid;
        this.kind = kind;
        this.theme = theme;

        this.article = article;
        this.knums = knums;
        this.cnums = cnums;
        this.c_date = c_date;

        this.custer = custer;
    }

}

