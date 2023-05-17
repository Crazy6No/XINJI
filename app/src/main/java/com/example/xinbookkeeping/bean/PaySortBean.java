package com.example.xinbookkeeping.bean;

public class PaySortBean {

    private PayBean data;
    private String name;
    private String post;

    public PaySortBean(PayBean data, String name, String post) {
        this.data = data;
        this.name = name;
        this.post = post;
    }

    public PayBean getData() {
        return data;
    }

    public void setData(PayBean data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
