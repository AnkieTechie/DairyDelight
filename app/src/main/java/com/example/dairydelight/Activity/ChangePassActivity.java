package com.example.dairydelight.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dairydelight.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    AppCompatButton cPass;
    AppCompatEditText currPass, newPass, conPass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pass);
        conPass = findViewById(R.id.conPass);
        cPass = findViewById(R.id.cPass);
        currPass = findViewById(R.id.currPass);
        newPass = findViewById(R.id.newPass);
        auth=FirebaseAuth.getInstance();


        cPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currPass.getText().toString();
                String newPassword = newPass.getText().toString();
                String confirmPassword = conPass.getText().toString();

                // Check if new passwords match
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (currentPassword.isEmpty()) {
                    currPass.setError("Current password is required");
                    return;
                }

                if (newPassword.isEmpty()) {
                    newPass.setError("New password is required");
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    conPass.setError("Please confirm the new password");
                    return;
                }

                // Check if new passwords match
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Proceed to update password
                updatePassword(currentPassword, newPassword);
            }
        });

    }

    private void updatePassword(String currentPassword, String newPassword) {

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Re-authenticate the user
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // After successful re-authentication, update the password
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(passwordUpdateTask -> {
                                        if (passwordUpdateTask.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
                                            finish();  // Close the activity after success
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Password update failed: " + passwordUpdateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Re-authentication failed
                            Toast.makeText(getApplicationContext(), "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User is not logged in
            Toast.makeText(getApplicationContext(), "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }
}