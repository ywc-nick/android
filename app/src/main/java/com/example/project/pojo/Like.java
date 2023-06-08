package com.example.project.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangwenchuan
 * @Date: 2022/11/24 20:15
 * Discretion: 喜欢
 */

public class Like implements Serializable {


    private int id;

    private Custer custer;//客户
    private Text text;//文章

    private String date;//日期

    public Like() {
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", custer=" + custer +
                ", text=" + text +
                ", date=" + date +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Custer getCuster() {
        return custer;
    }

    public void setCuster(Custer custer) {
        this.custer = custer;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Like(int id, Custer custer, Text text, String date) {
        this.id = id;
        this.custer = custer;
        this.text = text;
        this.date = date;
    }

    public Like(Custer custer, Text text) {
        this.custer = custer;
        this.text = text;
    }
}
