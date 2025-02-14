/*
package com.example.dairydelight.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.R;
import com.example.dairydelight.Activity.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MyProfileFragment extends Fragment {
    View view;
    Uri uri;
    String profileImageUr;
    private static final int PICK_IMAGE_REQUEST = 1;
    TextView myProf,myOrder,aboutUs,changePass,
            tnc,delAccount,username,useremail,logout;
    ImageView pUpload;
    FirebaseAuth fAuth;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;
    FirebaseUser user;

    String cachedName;
    String cachedEmail;
    String cachedProfileImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_my_profile, container, false);
        logout=view.findViewById(R.id.logout);
        myProf=view.findViewById(R.id.myProf);
        myOrder=view.findViewById(R.id.myOrder);
        aboutUs=view.findViewById(R.id.aboutUs);
        changePass=view.findViewById(R.id.changePass);
        tnc=view.findViewById(R.id.tnc);
        delAccount=view.findViewById(R.id.delAccount);
        username=view.findViewById(R.id.username);
        pUpload=view.findViewById(R.id.pUpload);
        useremail=view.findViewById(R.id.useremail);
        fAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firestore=FirebaseFirestore.getInstance();

        logout.setOnClickListener(v -> logout());
        if (cachedName == null) {
            loadProfile();
        } else {
            // Use cached data
            username.setText(cachedName);
            useremail.setText(cachedEmail);
            Picasso.get().load(cachedProfileImageUrl).into(pUpload);
        }
        pUpload.setOnClickListener(v -> openGallery());

        delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    showConfirmationDialog(user);
                } else {
                    Toast.makeText(getContext(), "No user is logged in", Toast.LENGTH_SHORT).show();
                }
            }

            private void showConfirmationDialog(FirebaseUser user) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure to delete your account permanently?")
                        .setPositiveButton("Yes", (dialog, which) -> deleteAccount(user))
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }

            private void deleteAccount(FirebaseUser user) {
                String uid=user.getUid();
                // Step 1: Delete user data from Firestore
                firestore.collection("user").document(uid).delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Step 2: Delete the user's Firebase Authentication account
                                user.delete()
                                        .addOnCompleteListener(authTask -> {
                                            if (authTask.isSuccessful()) {
                                                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                                navigateToLogin();
                                            } else {
                                                Toast.makeText(getContext(), "Error deleting account: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getContext(), "Error deleting user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
           uri=data.getData();
        pUpload.setImageURI(uri);
        uploadImageToFirebase();


    }

    private void uploadImageToFirebase() {
        FirebaseUser user=fAuth.getCurrentUser();
        if (user!=null &&uri!=null){
           String uid=user.getUid();
            StorageReference ref=firebaseStorage.getReference().child("profile/"+uid+".jpg");
            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                       ref.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                           String imgUri=downloadUri.toString();
                           saveImageToFirebase(uid,imgUri);
                       });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveImageToFirebase(String uid, String imgUri) {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("profileImageUrl", imgUri);  // Store the image URL in Firestore

        firestore.collection("user").document(uid)
                .update(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void loadProfile() {
        user=fAuth.getCurrentUser();
        if (user!=null){
            logout.setVisibility(View.VISIBLE);
            String userId=user.getUid();
            firestore.collection("user").document(userId).get().addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   DocumentSnapshot document=task.getResult();
                   if (document.exists()) {
                       String name=document.getString("name");
                       String email=document.getString("email");
                       profileImageUr=document.getString("profileImageUrl");
                       username.setText(name);
                       useremail.setText(email);
                       if (profileImageUr!=null){
                           Picasso.get()
                                   .load(profileImageUr)
                                   .into(pUpload);
                       }


                   }else {
                       Toast.makeText(getContext(), "user not exist", Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Toast.makeText(getContext(), "Error fetching user profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
            });
        }
    }

    private void logout() {
        fAuth.signOut();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideToolbar();
    }
}*/


package com.example.dairydelight.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dairydelight.Activity.ChangePassActivity;
import com.example.dairydelight.Activity.MainActivity;
import com.example.dairydelight.Activity.MyProfileActivity;
import com.example.dairydelight.Activity.SignInActivity;
import com.example.dairydelight.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MyProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private Uri uri;
    private TextView myProf, myOrder, aboutUs, changePass,LoginProf, tnc,
            delAccount, username, useremail, logout;
    private ImageView pUpload;
    private FirebaseAuth fAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    WebView webView;
    LinearLayout mainLayout;
    ImageButton closetbn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initializeUI();

        fAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        user = fAuth.getCurrentUser();

        isUserLoggedIn();

        setupClickListeners();

        // Configure the WebView
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient());

        // Load the Terms and Conditions HTML file from the assets folder


        return view;
    }

    private void isUserLoggedIn() {
        if (user != null) {
            logout.setVisibility(View.VISIBLE);
            loadProfileFromSharedPreferences(); // Check SharedPreferences first
        } else {
            LoginProf.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
    }
    }

    private void initializeUI() {
        logout = view.findViewById(R.id.logout);
        LoginProf = view.findViewById(R.id.LoginProf);
        username = view.findViewById(R.id.username);
        useremail = view.findViewById(R.id.useremail);
        pUpload = view.findViewById(R.id.pUpload);
        myProf = view.findViewById(R.id.myProf);
        myOrder = view.findViewById(R.id.myOrder);
        aboutUs = view.findViewById(R.id.aboutUs);
        changePass = view.findViewById(R.id.changePass);
        tnc = view.findViewById(R.id.tnc);
        delAccount = view.findViewById(R.id.delAccount);
       webView = view.findViewById(R.id.webView);
        mainLayout = view.findViewById(R.id.mainLayout);
        closetbn = view.findViewById(R.id.closetbn);


    }

    private void setupClickListeners() {
        LoginProf.setOnClickListener(v -> navigateToLogin());
        logout.setOnClickListener(v -> logout());
        myOrder.setOnClickListener(v -> {
            OrderFragment orderFragment=new OrderFragment();
            getFragmentManager().beginTransaction().replace(R.id.frame_layout,new OrderFragment())
                    .addToBackStack(null).commit();
        });
        myProf.setOnClickListener(v -> {
            if (user!=null){
                startActivity(new Intent(requireContext(), MyProfileActivity.class));
            }else {
                Toast.makeText(getContext(), "Login First", Toast.LENGTH_SHORT).show();
            }
        });

        pUpload.setOnClickListener(v -> {
            if (user!=null){
                openGallery();
            }else Toast.makeText(getContext(), "Login First", Toast.LENGTH_SHORT).show();
        });

        handleBackPressed();
        aboutUs.setOnClickListener(v -> {
            webView.loadUrl("file:///android_asset/about_us.html");
            mainLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            closetbn.setVisibility(View.VISIBLE);

        });
        closetbn.setOnClickListener(v -> {
            webView.setVisibility(View.GONE);
            closetbn.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        });
        tnc.setOnClickListener(v -> {
            webView.loadUrl("file:///android_asset/terms_and_conditions.html");
            mainLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            closetbn.setVisibility(View.VISIBLE);

        });
        changePass.setOnClickListener(v -> {
            if (user!=null){
                startActivity(new Intent(getContext(), ChangePassActivity.class));
            }else Toast.makeText(getContext(), "No Logged in", Toast.LENGTH_SHORT).show();
        });



        delAccount.setOnClickListener(v -> {
            if (user != null) {
                showConfirmationDialog(user);
            } else {
                Toast.makeText(getContext(), "No user is logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean handleBackPressed() {
        if (webView.getVisibility() == View.VISIBLE) {
            webView.setVisibility(View.GONE); // Hide WebView
            closetbn.setVisibility(View.GONE); // Hide WebView
            mainLayout.setVisibility(View.VISIBLE); // Show Button
            return true; // Consume the back press
        }
        return false; // Let the Activity handle the back press
    }

    private void loadProfileFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String profileImageUrl = sharedPreferences.getString("profileImageUrl", null);

        if (name != null && email != null) {
            username.setVisibility(View.VISIBLE);
            updateUI(name, email, profileImageUrl);
        } else {
            fetchAndSaveUserProfile();
        }
    }

    private void fetchAndSaveUserProfile() {
        String userId = user.getUid();
        firestore.collection("user").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString("name");
                    String email = document.getString("email");
                    String profileImageUrl = document.getString("profileImageUrl");

                    saveToSharedPreferences(name, email, profileImageUrl);
                    updateUI(name, email, profileImageUrl);
                } else {
                    Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Error fetching profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToSharedPreferences(String name, String email, String profileImageUrl) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("profileImageUrl", profileImageUrl);
        editor.apply();
    }

    private void updateUI(String name, String email, String profileImageUrl) {
        if (name != null) username.setText(name);
        if (email != null) useremail.setText(email);
        if (profileImageUrl != null) {
            Picasso.get().load(profileImageUrl).into(pUpload); // Load image with Picasso
        }
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            pUpload.setImageURI(uri);
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (user != null && uri != null) {
            String uid = user.getUid();
            StorageReference ref = firebaseStorage.getReference().child("profile/" + uid + ".jpg");

            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                            .addOnSuccessListener(downloadUri -> {
                                String profileImageUrl = downloadUri.toString();
                                saveImageToFirebaseAndSharedPreferences(uid, profileImageUrl);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to retrieve download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "User or image URI is null", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveImageToFirebaseAndSharedPreferences(String uid, String profileImageUrl) {
        if (uid == null || profileImageUrl == null) {
            Toast.makeText(getContext(), "Invalid user ID or profile image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("profileImageUrl", profileImageUrl);

        firestore.collection("user").document(uid).update(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveToSharedPreferences(null, null, profileImageUrl);
                        updateUI(username.getText().toString(), useremail.getText().toString(), profileImageUrl);
                        Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update profile image in Firestore: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showConfirmationDialog(FirebaseUser user) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure to delete your account permanently?")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount(user))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteAccount(FirebaseUser user) {
        String uid = user.getUid();
        firestore.collection("user").document(uid).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.delete().addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()) {
                                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                navigateToLogin();
                            } else {
                                Toast.makeText(getContext(), "Error deleting account: " + authTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Error deleting user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logout() {
        fAuth.signOut();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideToolbar();
    }
}

