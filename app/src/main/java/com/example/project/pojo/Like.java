package com.example.project.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangwenchuan
 * @Date: 2022/11/24 20:15
 * Discretion: 喜欢
 */

public class Like implements Serializable {

    private int tid;

    private Kind kind;


    private String theme;

    private String article;

    private int knums;  //点赞数 热门
    private int cnums;   //收藏数 推荐


    private Date c_date;//创建日期
    private Custer custer;//用户

    @Override
    public String toString() {
        return "Like{" +
                "tid=" + tid +
                ", kind=" + kind +
                ", theme='" + theme + '\'' +
                ", article='" + article + '\'' +
                ", knums=" + knums +
                ", cnums=" + cnums +
                ", c_date=" + c_date +
                ", custer=" + custer +
                '}';
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

    public Date getC_date() {
        return c_date;
    }

    public void setC_date(Date c_date) {
        this.c_date = c_date;
    }

    public Custer getCuster() {
        return custer;
    }

    public void setCuster(Custer custer) {
        this.custer = custer;
    }

    public Like(int tid, Kind kind, String theme, String article, int knums, int cnums, Date c_date, Custer custer) {
        this.tid = tid;
        this.kind = kind;
        this.theme = theme;
        this.article = article;
        this.knums = knums;
        this.cnums = cnums;
        this.c_date = c_date;
        this.custer = custer;
    }

    public Like() {
    }
}
