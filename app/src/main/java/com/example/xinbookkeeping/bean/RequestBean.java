package com.example.xinbookkeeping.bean;

public class RequestBean {
    private int id;

    private String userId;
    private String companyId;
    private String post;
    private long requestTime;

    private long time;
    private String operate;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequestBean(int id, String userId, String companyId, String post, long requestTime, long time,  String operate) {
        this.id = id;
        this.userId = userId;
        this.companyId = companyId;
        this.post = post;
        this.requestTime = requestTime;
        this.time = time;
        this.operate = operate;
    }

    public RequestBean(int id, String userId, String companyId, String post, long requestTime, long time, String operate, String name) {
        this.id = id;
        this.userId = userId;
        this.companyId = companyId;
        this.post = post;
        this.requestTime = requestTime;
        this.time = time;
        this.operate = operate;
        this.name = name;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static final String OPERATE_ING = "1";
    public static final String OPERATE_AGREE = "2";
    public static final String OPERATE_REFUSE = "3";
    public static final String OPERATE_DONE = "4";

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
