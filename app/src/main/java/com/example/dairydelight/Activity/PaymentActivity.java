package com.example.dairydelight.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dairydelight.R;

public class PaymentActivity extends AppCompatActivity {
    LinearLayout cashonLine;
    TextView cod,wallet_upi,total;
    ImageView check;
    AppCompatButton payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cashonLine=findViewById(R.id.cashonLine);
        cod=findViewById(R.id.cod);
        total=findViewById(R.id.total);
        check=findViewById(R.id.check);
        payment=findViewById(R.id.payment);
        wallet_upi=findViewById(R.id.wallet_upi);
        String val=getIntent().getStringExtra("total");
        total.setText(val);


        // Set default button state (disabled)
        payment.setEnabled(false);
        payment.setBackgroundResource(R.drawable.btn_disabled);

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.getVisibility()==View.VISIBLE){
                    check.setVisibility(View.GONE);
                    payment.setEnabled(false); // Disable button
                    payment.setBackgroundResource(R.drawable.btn_disabled); // Set disabled background
                    payment.setTextColor(getResources().getColor(R.color.grayblack));


                }else {
                    check.setVisibility(View.VISIBLE);
                    payment.setEnabled(true); // Enable button
                    payment.setBackgroundResource(R.drawable.btn_enabled); // Set enabled background
                    payment.setTextColor(getResources().getColor(R.color.white));

                }
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

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
                                               }
                                               startActivity(new Intent(getApplicationContext(), PlaceOrderActivity.class));
                                               /*new AlertDialog.Builder(PaymentActivity.this)
                                                       .setTitle("Order Successful")
                                                       .setMessage("Your order has been placed successfully!")
                                                       .setIcon(R.drawable.right) // Set your success icon here
                                                       .setPositiveButton("OK", null)
                                                       .show();*/
                                           }, 5000);
                                       }
                                   });

                wallet_upi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(PaymentActivity.this, RazorPaymentActivity.class).putExtra("ptotal",val));
                    }
                });
    }
}