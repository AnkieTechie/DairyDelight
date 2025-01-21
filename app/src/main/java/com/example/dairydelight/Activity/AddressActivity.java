package com.example.dairydelight.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.dairydelight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddressActivity extends AppCompatActivity {
    AppCompatEditText fname, sAddress, city, pin, landmark, state, mob;
    Spinner select_country;
    CheckBox lo_checkbox;
    AppCompatButton savecontinue;
    String total;
    ImageView delAddress;
    AppCompatTextView AddressName;
    LinearLayout savedAddressView,formLayout;
    String savedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // Initialize views
        initializeViews();

        // Check if the address is already saved
        checkSavedAddress();

        savecontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    if (formLayout.getVisibility() == View.VISIBLE) {
                        // Form is visible, validate inputs and save the address
                        if (isValidInput()) {
                            saveAddress(); // Save the address in SharedPreferences
                            navigateToPaymentActivity(); // Navigate to the next activity
                        }
                    } else if (savedAddressView.getVisibility() == View.VISIBLE) {
                        // Saved address is being used, directly navigate to PaymentActivity
                        navigateToPaymentActivity();
                    }
                } else {
                    // User is not logged in, show a Toast message
                    Toast.makeText(getApplicationContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
                }



            }
        });


        delAddress.setOnClickListener(v -> delAddress());
    }

    // Initialize views
    private void initializeViews() {
        fname = findViewById(R.id.fname);
        delAddress = findViewById(R.id.delAddress);
        formLayout = findViewById(R.id.formLayout);
        AddressName = findViewById(R.id.AddressName);
        savedAddressView = findViewById(R.id.savedAddressView);
        sAddress = findViewById(R.id.sAddress);
        city = findViewById(R.id.city);
        pin = findViewById(R.id.pin);
        landmark = findViewById(R.id.landmark);
        state = findViewById(R.id.state);
        mob = findViewById(R.id.mob);
        select_country = findViewById(R.id.select_country);
        savecontinue = findViewById(R.id.savecontinue);
        lo_checkbox = findViewById(R.id.lo_checkbox);

        String[] countryList = {"Select your country", "India"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_country.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra("finalTotal")) {
            total = intent.getStringExtra("finalTotal");
        } else if (intent.hasExtra("dprice")) {
            total = intent.getStringExtra("dprice");
        } else if (intent.hasExtra("itemPrice")) {
            total = intent.getStringExtra("itemPrice");
        } else {
            total = intent.getStringExtra("caraPrice");
        }
    }

    // Check if the address is already saved and update UI
    private void checkSavedAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences("AddressDetails", MODE_PRIVATE);
        savedAddress = sharedPreferences.getString("fullAddress", null);

        if (savedAddress != null) {
            // Show saved address and hide form
            formLayout.setVisibility(View.GONE);
            savedAddressView.setVisibility(View.VISIBLE);
            AddressName.setText(savedAddress);
        } else {
            // Show form and hide saved address view
            formLayout.setVisibility(View.VISIBLE);
            savedAddressView.setVisibility(View.GONE);
        }
    }

    // Save the address in SharedPreferences
    private void saveAddress() {
        String fullAddress = fname.getText().toString() + ", " +
                sAddress.getText().toString() + ", " +
                city.getText().toString() + ", " +
                pin.getText().toString() + ", " +
                landmark.getText().toString() + ", " +
                state.getText().toString() + ", " +
                mob.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("AddressDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullAddress", fullAddress);
        editor.apply();

        // Show saved address and hide form
        formLayout.setVisibility(View.GONE);
        savedAddressView.setVisibility(View.VISIBLE);
        AddressName.setText(fullAddress);
    }

    // Navigate to PaymentActivity
    private void navigateToPaymentActivity() {
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class)
                .putExtra("total", total);
        startActivity(intent);
    }


    private void delAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences("AddressDetails", MODE_PRIVATE);

        // Check if the key exists before trying to delete
        if (sharedPreferences.contains("fullAddress")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("fullAddress");
            editor.apply();

            // Show a message
            Toast.makeText(this, "Address deleted successfully!", Toast.LENGTH_SHORT).show();

            // Update UI to show the form
            formLayout.setVisibility(View.VISIBLE);
            savedAddressView.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "No address found to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidInput() {
        // Validate name
        if (fname.getText().toString().trim().isEmpty()) {
            fname.setError("Full name is required");
            return false;
        }

        // Validate street address
        if (sAddress.getText().toString().trim().isEmpty()) {
            sAddress.setError("Street address is required");
            return false;
        }

        // Validate city
        if (city.getText().toString().trim().isEmpty()) {
            city.setError("City is required");
            return false;
        }

        // Validate pin code
        if (pin.getText().toString().trim().isEmpty()) {
            pin.setError("Pin code is required");
            return false;
        } else if (pin.getText().toString().length() != 6) {
            pin.setError("Pin code must be 6 digits");
            return false;
        }

        // Validate mobile number
        if (mob.getText().toString().trim().isEmpty()) {
            mob.setError("Mobile number is required");
            return false;
        } else if (mob.getText().toString().length() != 10) {
            mob.setError("Mobile number must be 10 digits");
            return false;
        }

        // Validate country selection
        if (select_country.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a country", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate terms and conditions checkbox
        if (!lo_checkbox.isChecked()) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
