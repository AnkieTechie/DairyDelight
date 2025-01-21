package com.example.dairydelight.Retrofit;

import com.google.gson.annotations.SerializedName;

public class UserIdRequest {
    @SerializedName("user_id")
    private String userId;

    public UserIdRequest(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
