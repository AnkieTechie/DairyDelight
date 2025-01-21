package com.example.dairydelight.Models;

import java.io.Serializable;

public class CartModel implements Serializable {

    private int id;
    private String cImage;
    private String cTitle;
    private String cDescription;
    private double cPrice;
    private int Cquantity;
    private boolean isAddedToCart;

    // Constructor
    public CartModel(String cImage, String cTitle, String cDescription, double cPrice, int Cquantity) {
        this.cImage = cImage;
        this.cTitle = cTitle;
        this.cDescription = cDescription;
        this.cPrice = cPrice;
        this.Cquantity = Cquantity;
        this.isAddedToCart = true; // Assuming it's added to the cart when created
    }

    // Default constructor
    public CartModel() {}

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcImage() {
        return cImage;
    }

    public void setcImage(String cImage) {
        this.cImage = cImage;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public String getcDescription() {
        return cDescription;
    }

    public void setcDescription(String cDescription) {
        this.cDescription = cDescription;
    }

    public double getcPrice() {
        return cPrice;
    }

    public void setcPrice(double cPrice) {
        this.cPrice = cPrice;
    }

    public int getCquantity() {
        return Cquantity;
    }

    public void setCquantity(int Cquantity) {
        this.Cquantity = Cquantity;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }
}
