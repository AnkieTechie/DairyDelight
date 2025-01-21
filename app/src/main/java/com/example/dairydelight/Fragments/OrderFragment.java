package com.example.dairydelight.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.Adapter.OrderAdapter;
import com.example.dairydelight.R;
import com.example.dairydelight.Retrofit.ApiClient;
import com.example.dairydelight.Retrofit.ApiServices;
import com.example.dairydelight.Retrofit.OrderResponse;
import com.example.dairydelight.Retrofit.UserIdRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            ApiServices apiService = ApiClient.getRetrofit().create(ApiServices.class);
            UserIdRequest userIdRequest = new UserIdRequest("1");
            Call<OrderResponse> call = apiService.getOrders(userIdRequest);
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Handle the successful response
                        OrderResponse orderResponse = response.body();
                        Log.d("API Response", "Message: " + orderResponse.getMessage());

                        // Set the RecyclerView adapter with the orders list
                        orderAdapter = new OrderAdapter(orderResponse.getOrders(), getContext());
                        recyclerView.setAdapter(orderAdapter);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable throwable) {

                }

            });

        } else { Toast.makeText(getContext(), "Not logged in", Toast.LENGTH_SHORT).show();}
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideToolbar();
    }
}