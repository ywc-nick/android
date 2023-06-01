package com.example.project.pojo;


import java.io.Serializable;
import java.sql.Date;

/**
 * @Author: yangwenchuan
 * @Date: 2022/11/7 22:01
 * Discretion:
 */

public class Custer implements Serializable {

    private int id;

    private String username;

    private String password;
    private String name;

    private String vir_name;

    private String face;


    private String sex;

    private Date birth;

    @Override
    public String toString() {
        return "Custer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", vir_name='" + vir_name + '\'' +
                ", face='" + face + '\'' +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                ", nums=" + nums +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVir_name() {
        return vir_name;
    }

    public void setVir_name(String vir_name) {
        this.vir_name = vir_name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getNums() {
        return nums;
    }

    public void setNums(Long nums) {
        this.nums = nums;
    }

    public Custer() {
    }

    public Custer(int id, String username, String password, String name, String vir_name, String face, String sex, Date birth, String phone, Long nums) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.vir_name = vir_name;
        this.face = face;
        this.sex = sex;
        this.birth = birth;
        this.phone = phone;
        this.nums = nums;
    }

    private String phone;
    private Long nums;//文章数


}
