package com.example.project.pojo;


import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangwenchuan
 * @Date: 2022/11/24 20:14
 * Discretion: 收藏
 */

public class AppCollectText implements Serializable {

    private int id;

    private Custer custer;//客户

    private Text text;//文章

    @Override
    public String toString() {
        return "AppCollectText{" +
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AppCollectText() {
    }

    public AppCollectText(int id, Custer custer, Text text, Date date) {
        this.id = id;
        this.custer = custer;
        this.text = text;
        this.date = date;
    }

    private Date date;//日期



}
