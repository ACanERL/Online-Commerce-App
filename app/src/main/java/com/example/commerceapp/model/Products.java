package com.example.commerceapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Products implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("price")
    public double price;
    @SerializedName("description")
    public String description;
    @SerializedName("category")
    public String category;
    @SerializedName("image")
    public String image;
    @SerializedName("rating")
    public Rating rating;
    public float totalPrice;
    public String key;
    public int count=0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }



}
