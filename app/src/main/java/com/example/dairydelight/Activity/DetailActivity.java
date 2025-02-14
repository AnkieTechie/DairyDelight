package com.example.dairydelight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dairydelight.Models.TabModel;
import com.example.dairydelight.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    ImageView detail_img;
    AppCompatButton buyNow;
    TextView price;
    String dprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detail_img=findViewById(R.id.detail_img);

        TabModel productModel = (TabModel) getIntent().getSerializableExtra("productModel");

        // Display the model data (Example)
        TextView nameTextView = findViewById(R.id.detail_Title);
        TextView descriptionTextView = findViewById(R.id.descr);
        price=findViewById(R.id.price);
        buyNow=findViewById(R.id.buyNow);

        if (productModel != null) {
            nameTextView.setText(productModel.getTitle());
            dprice=String.valueOf(productModel.getPrice());
            price.setText(dprice);
            descriptionTextView.setText(productModel.getDescription());
            Picasso.get()
                    .load(productModel.getImage())
                    .into(detail_img);
        }

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this,AddressActivity.class)
                        .putExtra("dprice",dprice)
                        .putExtra("prodTitle",productModel.getTitle())
                        .putExtra("pimage",productModel.getImage()));
            }
        });
    }
}