package com.example.dairydelight.Retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {

    @POST("just-academy-project/api/auth/list-order")
    Call<OrderResponse> getOrders(@Body UserIdRequest userIdRequest);
}
