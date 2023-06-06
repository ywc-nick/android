package com.example.project.sqlite.pojo;

public class TextHistoryBean {

    public int tid;
    public String title;
    public String time;
    public Integer rate;

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

    public TextHistoryBean(String title, Integer rate) {
        this.title = title;
        this.rate = rate;
    }

    public TextHistoryBean() {
    }

    public TextHistoryBean(int tid, String title, String timestamp, Integer rate) {
        this.tid = tid;
        this.title = title;
        this.time = timestamp;
        this.rate = rate;
    }
}
