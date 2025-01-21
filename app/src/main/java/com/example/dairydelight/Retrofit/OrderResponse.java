package com.example.dairydelight.Retrofit;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("orders")
    private List<Order> orders;  // List of Order objects

    // Getter and Setter methods for message and orders
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    // Inner class representing each individual order
    public static class Order {
        @SerializedName("id")
        private int id;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("product_id")
        private String productId;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("product_titles")
        private List<String> productTitles; // List of product titles

        // Getter and Setter methods for each field
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<String> getProductTitles() {
            return productTitles;
        }

        public void setProductTitles(List<String> productTitles) {
            this.productTitles = productTitles;
        }
    }
}
