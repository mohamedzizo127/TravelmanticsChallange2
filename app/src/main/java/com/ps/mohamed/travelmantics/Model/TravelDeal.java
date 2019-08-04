package com.ps.mohamed.travelmantics.Model;


public class TravelDeal {
    private String title;
    private String desc;
    private String image;
    private String value;
    private String userId;

    public TravelDeal() {
    }

    public TravelDeal(String title, String desc, String image, String value, String userId) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.value = value;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}