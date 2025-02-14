package com.example.dairydelight.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dairydelight.Models.Order;
import com.example.dairydelight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    LinearLayout cashonLine;
    TextView cod, wallet_upi, total;
    ImageView check;
    AppCompatButton payment;
    String title, image, address, val, OrderId;
    String status = "pending";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    String paymentStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cashonLine = findViewById(R.id.cashonLine);
        cod = findViewById(R.id.cod);
        total = findViewById(R.id.total);
        check = findViewById(R.id.check);
        payment = findViewById(R.id.payment);
        wallet_upi = findViewById(R.id.wallet_upi);
        user = auth.getCurrentUser();

        val = getIntent().getStringExtra("total");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        address = getIntent().getStringExtra("address");
        total.setText(val);

        // Set default button state (disabled)
        payment.setEnabled(false);
        payment.setBackgroundResource(R.drawable.btn_disabled);

        cod.setOnClickListener(v -> {
            if (check.getVisibility() == View.VISIBLE) {
                check.setVisibility(View.GONE);
                payment.setEnabled(false); // Disable button
                payment.setBackgroundResource(R.drawable.btn_disabled); // Set disabled background
                payment.setTextColor(getResources().getColor(R.color.grayblack));


            } else {
                check.setVisibility(View.VISIBLE);
                payment.setEnabled(true); // Enable button
                payment.setBackgroundResource(R.drawable.btn_enabled); // Set enabled background
                payment.setTextColor(getResources().getColor(R.color.white));

            }
        });
        payment.setOnClickListener(v -> {
            long timestamp = System.currentTimeMillis();  // Get the current timestamp in milliseconds
            OrderId = "ORD" + timestamp;
            paymentStatus = "cash on delivery";
            String userId = user.getUid();
            Order order = new Order(OrderId, title, image, address, val, status, paymentStatus);
            db.collection("user").document(userId).collection("order").add(order)
                    .addOnSuccessListener(documentReference -> {

                        Log.d("Firestore", "Order placed successfully with payment status: " + paymentStatus);
                    });

            // Show loading dialog
            ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this);
            progressDialog.setMessage("Processing your order...");
            progressDialog.setCancelable(false); // Prevent cancellation
            progressDialog.show();

            // Wait for 5 seconds before showing the success dialog
            new Handler().postDelayed(() -> {
                // Dismiss the loading dialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    goToPlaceOrder();
                }
            }, 5000);
        });

        wallet_upi.setOnClickListener(v -> {
            savetoFirestore();
            razorPay();
        });
    }

    private void goToPlaceOrder() {
        startActivity(new Intent(this, PlaceOrderActivity.class));
    }

    private void savetoFirestore() {
        String userId = user.getUid();
        paymentStatus = "Paid Online";
        Order order = new Order(OrderId, title, image, address, val, status, paymentStatus);
        db.collection("user").document(userId).collection("order").add(order)
                .addOnSuccessListener(documentReference -> {

                    Log.d("Firestore", "Order placed successfully with payment status: " + paymentStatus);
                });
    }

    private void razorPay() {

        Double numericValue = Double.parseDouble(val);
        Double result = numericValue * 100;
        long timestamp = System.currentTimeMillis();  // Get the current timestamp in milliseconds
        OrderId = "ORD" + timestamp;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_FJWs7HNQPkXE9s");  // Use your Razorpay Key ID

        try {
            JSONObject options = new JSONObject();
            options.put("nameOfProduct", title);
            options.put("description", "");
            options.put("currency", "INR"); // Use appropriate currency code
            options.put("amount", result); // Amount in paise (i.e., 10000 paise = 100 INR)
            options.put("orderId", OrderId);
            options.put("prefill.email", user.getEmail());

            // Open Razorpay Checkout
            checkout.open(PaymentActivity.this, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        goToPlaceOrder();
        Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("Payment Error", "Error Code: " + i + ", Response: " + s);
        Toast.makeText(this, "Payment Failed: " + s, Toast.LENGTH_SHORT).show();

    }
}