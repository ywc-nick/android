package com.example.project.sqlite.pojo;

import com.example.project.util.TimeUtils;

import java.io.Serializable;

public class TextHistoryBean implements Serializable {

    private int tid;
    private String title;
    private String time;
    private Integer rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    @Override
    public String toString() {
        return "TextHistoryBean{" +
                "tid=" + tid +
                ", title='" + title + '\'' +
                ", timestamp=" + time +
                ", rate=" + rate +
                '}';
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String timestamp) {
        this.time = timestamp;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public TextHistoryBean(int tid,String title, Integer rate) {
        this.title = title;
        this.rate = rate;
        this.tid = tid;
    }

    public TextHistoryBean() {
    }

    public TextHistoryBean(Integer id, String title, String time, Integer rate, int tid) {
        this.tid = tid;
        this.title = title;
        this.time = time;
        this.rate = rate;
        this.id = id;
    }
}
