package com.example.xinbookkeeping.bean;

import android.text.TextUtils;

public class PayBean {

    private int id;
    private String userId;
    private String companyId;
    private String money;

    private int year;

    private int month;
    private int day;
    private long time;

    public PayBean(int id, String userId, String companyId, String money, int year, int month, int day, long time) {
        this.id = id;
        this.userId = userId;
        this.companyId = companyId;
        this.money = money;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
