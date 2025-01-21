package com.example.dairydelight.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dairydelight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MyProfileActivity extends AppCompatActivity {
    ImageView mypUpload;
    TextView name1, email1, myAddress;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        myAddress = findViewById(R.id.myAddress);
        mypUpload = findViewById(R.id.mypUpload);
        name1 = findViewById(R.id.name1);
        email1 = findViewById(R.id.email1);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String name = sharedPreferences.getString("name", null);
            String email = sharedPreferences.getString("email", null);
            String profileImageUrl = sharedPreferences.getString("profileImageUrl", null);

            if (name != null) name1.setText(name);
            if (email != null) email1.setText(email);
            if (profileImageUrl != null) {
                Picasso.get().load(profileImageUrl).into(mypUpload); // Load image with Picasso
            } else {
                Toast.makeText(this, "No data Found ", Toast.LENGTH_SHORT).show();
            }

        } else Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
    }
}