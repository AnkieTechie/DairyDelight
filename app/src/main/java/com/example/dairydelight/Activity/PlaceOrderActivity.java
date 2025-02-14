package com.example.dairydelight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dairydelight.Fragments.OrderFragment;
import com.example.dairydelight.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class PlaceOrderActivity extends AppCompatActivity {
    AppCompatButton myOrder;
    TextView name;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_order);
        myOrder=findViewById(R.id.myOrder);
        name=findViewById(R.id.name);

        String name2=auth.getCurrentUser().getDisplayName();
        name.setText(name2);

        myOrder.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra("show_my_order",true));
        });
    }
}