package com.example.commerceapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable {
    @SerializedName("rate")
    public double rate;
    @SerializedName("count")
    public int count;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
