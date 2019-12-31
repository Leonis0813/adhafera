package com.leonis.android.adhafera.models;

import java.util.Date;

/**
 * Created by leonis on 2018/12/30.
 */

public class Payment {
    private final Date date;
    private final String content;
    private final String[] categories;
    private final int price;

    public Payment(Date date, String content, String[] categories, int price, String paymentType) {
        this.date = date;
        this.content = content;
        this.categories = categories;
        this.price = paymentType.equals("income") ? price : -1 * price;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String[] getCategories() {
        return categories;
    }

    public int getPrice() {
        return price;
    }
}
