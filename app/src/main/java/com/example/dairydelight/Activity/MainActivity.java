package com.example.dairydelight.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.dairydelight.Fragments.HomFragment;
import com.example.dairydelight.Fragments.MyCartFragment;
import com.example.dairydelight.Fragments.MyProfileFragment;
import com.example.dairydelight.Fragments.OrderFragment;
import com.example.dairydelight.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView nav_bottom;
    ImageView cartIcon;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartIcon=findViewById(R.id.cartIcon);
        nav_bottom=findViewById(R.id.nav_bottom);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setnavigation();
        // Check if the "show_my_order" flag is true in the Intent
        boolean showMyOrder = getIntent().getBooleanExtra("show_my_order", false);

        // If this is the first time Activity B is being created, show the Home Fragment or My Order
        if (savedInstanceState == null) {
            if (showMyOrder) {
                // If the flag is set, show "My Order" fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout,new OrderFragment()).commit();
            } else {
                // Otherwise, show the Home Fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout,new HomFragment()).commit();
            }
        }

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCartFragment myCartFragment = new MyCartFragment();

                // Replace the current fragment in the fragment container with the new fragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, myCartFragment) // Use the ID of the fragment container
                        .addToBackStack(null) // Allow going back to the previous fragment
                        .commit(); // Commit the transaction
            }
        });
    }
    public void showToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    public void hideToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    private void setnavigation() {
        nav_bottom.setOnItemSelectedListener(item -> {
            Fragment fragment=new HomFragment();
            int item_id=item.getItemId();
            if (item_id==R.id.nave_home){
                fragment=new HomFragment();
            } else if (item_id==R.id.nav_chat) {
                fragment=new OrderFragment();
            } else if (item_id==R.id.nav_cart) {
                fragment=new MyCartFragment();
            } else if (item_id==R.id.nav_profile) {
                fragment=new MyProfileFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment).commit();
            return true;
        });
    }
}