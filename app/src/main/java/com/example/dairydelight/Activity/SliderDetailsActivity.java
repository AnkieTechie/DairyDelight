package com.example.dairydelight.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dairydelight.R;
import com.squareup.picasso.Picasso;

public class SliderDetailsActivity extends AppCompatActivity {
    ImageView Sdetail_img;
    TextView Sdetail_Title,price;
    AppCompatButton SliderBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_details);

        Sdetail_Title=findViewById(R.id.Sdetail_Title);
        Sdetail_img=findViewById(R.id.Sdetail_img);
        price=findViewById(R.id.price);
        SliderBuyNow=findViewById(R.id.SliderBuyNow);

        String imageUri=getIntent().getStringExtra("imageUri");
        String title=getIntent().getStringExtra("title");
        int pric=getIntent().getIntExtra("priceee",-1);
        String pri=String.valueOf(pric);

        Sdetail_Title.setText(title);
        price.setText(pri);
        Picasso.get().load(imageUri).into(Sdetail_img);

        SliderBuyNow.setOnClickListener(v ->
                startActivity(new Intent(SliderDetailsActivity.
                this, AddressActivity.class).putExtra("caraPrice",pri)
                .putExtra("img", (CharSequence) Sdetail_img)
                .putExtra("title", (CharSequence) Sdetail_Title)));
    }
}