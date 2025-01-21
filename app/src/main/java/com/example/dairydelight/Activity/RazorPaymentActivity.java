package com.example.dairydelight.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dairydelight.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class RazorPaymentActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_payment);
        TextView pay=findViewById(R.id.pay);

        String ftotal=getIntent().getStringExtra("ptotal");
                Double numericValue = Double.parseDouble(ftotal);

                // Multiply the numeric value by 100
                Double result = numericValue * 100;
        // Initialize Razorpay Checkout
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_FJWs7HNQPkXE9s");  // Use your Razorpay Key ID

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Dairy Delight");
            options.put("description", "For shopping milk");
            options.put("currency", "INR"); // Use appropriate currency code
            options.put("amount", result); // Amount in paise (i.e., 10000 paise = 100 INR)
            options.put("prefill.email", "customer@example.com");
            options.put("prefill.contact", "9876543210");

            // Open Razorpay Checkout
            checkout.open(RazorPaymentActivity.this, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful: "+ s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("Payment Error", "Error Code: " + i + ", Response: " + s);
        Toast.makeText(this, "Payment Failed: " +s, Toast.LENGTH_SHORT).show();

    }
}