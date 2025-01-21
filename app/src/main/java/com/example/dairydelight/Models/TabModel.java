package com.example.dairydelight.Models;

import java.io.Serializable;

public class TabModel implements Serializable {
    private int id;
    private String title,description;
    private int price;
    private String image;

    public TabModel(int id, String title, String description, int price, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
