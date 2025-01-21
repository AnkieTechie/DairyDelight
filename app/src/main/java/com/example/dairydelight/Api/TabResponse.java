package com.example.dairydelight.Api;

import com.example.dairydelight.Models.TabModel;

import java.util.List;

public class TabResponse {
    private List<TabModel> products;

    public TabResponse(List<TabModel> products) {
        this.products = products;
    }

    public List<TabModel> getProducts() {
        return products;
    }

    public void setProducts(List<TabModel> products) {
        this.products = products;
    }
}
