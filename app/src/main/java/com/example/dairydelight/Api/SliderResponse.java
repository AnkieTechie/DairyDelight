package com.example.dairydelight.Api;

import com.example.dairydelight.Models.SliderModel;

import java.util.List;

public class SliderResponse {
    private List<SliderModel> products;

    public SliderResponse(List<SliderModel> products) {
        this.products = products;
    }

    public List<SliderModel> getProducts() {
        return products;
    }

    public void setProducts(List<SliderModel> products) {
        this.products = products;
    }
}
