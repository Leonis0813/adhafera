package com.leonis.android.adhafera.service;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class InputChecker {
    private final Pattern datePattern;
    private final Pattern pricePattern;

    public InputChecker() {
        datePattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        pricePattern = Pattern.compile("^[1-9]\\d*$");
    }

    public ArrayList<Integer> checkEmpty(String[] inputs) {
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i=0;i<inputs.length;i++) {
            if(inputs[i] == null || inputs[i].equals("")) {
                ids.add(i);
            }
        }
        return ids;
    }

    public boolean checkDate(String date) {
        return datePattern.matcher(date).find();
    }

    public boolean checkPrice(String price) {
        return pricePattern.matcher(price).find();
    }
}
