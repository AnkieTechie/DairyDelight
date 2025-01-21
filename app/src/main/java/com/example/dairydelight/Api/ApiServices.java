package com.example.dairydelight.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {
    @POST("product")
    Call<SliderResponse> getSlider(@Body SliderRequest sliderRequest);

    @POST("product")
    Call<TabResponse> getTabs(@Body TabRequest tabRequest);
}
