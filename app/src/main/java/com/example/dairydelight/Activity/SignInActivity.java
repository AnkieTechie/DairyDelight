package com.example.dairydelight.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dairydelight.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {
    CheckBox log_checkbox;
    ActivitySignInBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null) {
            // User is already signed in, redirect to the main activity
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Close the login activity so the user can't go back to it
        }

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Login..pls wait");
        progressDialog.setCancelable(false);

        binding.skip.setOnClickListener(v -> skipped());
        binding.logSignUp.setOnClickListener(v -> noAccount());
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.logEmail.getText().toString().trim();
                String password = binding.logPassword.getText().toString().trim();
                if (email.isEmpty()) {
                    binding.logEmail.setError("Email is required");
                    binding.logEmail.requestFocus();
                    return;

                }
                if (password.isEmpty()) {
                    binding.logPassword.setError("Password is required");
                    binding.logPassword.requestFocus();
                    return;
                }
                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(password)) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!binding.logCheckbox.isChecked()) {
                    // Checkbox is checked
                    Toast.makeText(getApplicationContext(), "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                haveAccount(email, password);
            }
        });


    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void skipped() {
        startActivity(new Intent(SignInActivity.this,MainActivity.class));
    }

    private void noAccount() {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
    }
    private void haveAccount(String email,String password) {
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                // Login successful
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
            } else {
                // Login failed, show error message
                Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isValidPassword(String password) {
        return password.length()>=6;
    }
}
