package com.leonis.android.adhafera.models;

import java.util.Date;

/**
 * Created by leonis on 2018/12/30.
 */

public class Payment {
    private Date date;
    private String content;
    private String[] categories;
    private int price;
    private String paymentType;

    public Payment(Date date, String content, String[] categories, int price, String paymentType) {
        this.date = date;
        this.content = content;
        this.categories = categories;
        this.price = price;
        this.paymentType  = paymentType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
