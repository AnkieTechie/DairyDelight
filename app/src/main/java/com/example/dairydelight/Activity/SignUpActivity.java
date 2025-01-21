package com.example.dairydelight.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dairydelight.Models.User;
import com.example.dairydelight.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Signing up..pls wait");
        progressDialog.setCancelable(false);

        binding.logsignIn.setOnClickListener(v ->
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class)));
        binding.signUp.setOnClickListener(v -> ButtonSignUp());

    }

    private void ButtonSignUp() {
        String name = binding.regName.getText().toString().trim();
        String email = binding.regEmail.getText().toString().trim();
        String password = binding.regPassword.getText().toString().trim();
        if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
            if (!binding.regCheckbox.isChecked()) {
                // Checkbox is checked
                Toast.makeText(getApplicationContext(), "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show();
                return;
        }
        progressDialog.show();
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               String userId=fAuth.getCurrentUser().getUid();
               User user =new User(name, email);
               firestore.collection("user").document(userId).set(user).addOnCompleteListener(task1 -> {
                   progressDialog.dismiss();
                  if (task1.isSuccessful()){
                      Toast.makeText(this,"Signup Successful", Toast.LENGTH_SHORT).show();
                      binding.regName.setText("");
                      binding.regEmail.setText("");
                      binding.regPassword.setText("");
                  } else {
                      Toast.makeText(this, "Failed to Store data", Toast.LENGTH_SHORT).show();
                  }
               });
               Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
               startActivity(intent);
               finish();

           }else {
               progressDialog.dismiss();
               Toast.makeText(this, "SignUp failed", Toast.LENGTH_SHORT).show();
           }
        });

    }

}