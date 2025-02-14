package com.example.dairydelight.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairydelight.Activity.AddressActivity;
import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.Activity.PaymentActivity;
import com.example.dairydelight.Adapter.CartAdapter;
import com.example.dairydelight.Database.CartDatabaseHelper;
import com.example.dairydelight.Models.CartModel;
import com.example.dairydelight.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {
    View view;
    TextView ctotal, emptyCart;
    RecyclerView cartRecyclerView;
    CartAdapter cartAdapter;
    CartDatabaseHelper databaseHelper;
    AppCompatButton checkout;
    List<CartModel> cartItemList;
    LinearLayout checkLinear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        checkout = view.findViewById(R.id.checkout);
        emptyCart = view.findViewById(R.id.emptyCart);
        ctotal = view.findViewById(R.id.ctotal);
        checkLinear = view.findViewById(R.id.checkLinear);

        // Fetch all cart items from the database
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), ctotal, cartItemList);
        cartRecyclerView.setAdapter(cartAdapter);

        databaseHelper = new CartDatabaseHelper(getContext());
        loadCartItems();
        checkout.setOnClickListener(v -> {
            if (cartItemList.isEmpty()) {
                Toast.makeText(getContext(), "No items available to checkout", Toast.LENGTH_SHORT).show();

            } else {
                String ct = ctotal.getText().toString();
                Intent intent = new Intent(getContext(), AddressActivity.class).
                        putExtra("finalTotal", ct)
                        .putExtra("cartList", (Serializable) cartItemList);
                startActivity(intent);
            }

        });

        return view;
    }

    private void loadCartItems() {
        cartItemList.clear();
        cartItemList.addAll(databaseHelper.getAllCartItems());
        cartAdapter.updateData(cartItemList);
        cartAdapter.notifyDataSetChanged();
        if (cartItemList.isEmpty()) {
            emptyCart.setVisibility(View.VISIBLE);
            checkLinear.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {
            emptyCart.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkLinear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideToolbar();
        loadCartItems();
    }
}