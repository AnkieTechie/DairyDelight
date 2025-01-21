package com.example.dairydelight.Api;

public class SliderRequest {
    String categories_id;

    public SliderRequest(String categories_id) {
        this.categories_id = categories_id;
    }

    public String getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(String categories_id) {
        this.categories_id = categories_id;
    }
}
