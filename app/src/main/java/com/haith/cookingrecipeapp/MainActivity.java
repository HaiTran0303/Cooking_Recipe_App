package com.haith.cookingrecipeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.haith.cookingrecipeapp.R;
import com.google.firebase.FirebaseApp;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView navHeaderName, navHeaderEmail,forgotPwdText,uploadAvatarText;
    private Button logoutButton;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageAvatarView;
    private Uri avatarUri; // Store selected image URI

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
    private void uploadAvatarToFirebase() {
        if (avatarUri != null) {
            String userId = mAuth.getCurrentUser().getUid();
            StorageReference avatarRef = FirebaseStorage.getInstance().getReference("avatars/" + userId + ".jpg");

            avatarRef.putFile(avatarUri)
                    .addOnSuccessListener(taskSnapshot -> avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String avatarUrl = uri.toString();
                        saveAvatarUrlToFirestore(avatarUrl); // Save URL to Firestore
                    }))
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to upload avatar", Toast.LENGTH_SHORT).show());
        }
    }
    private void saveAvatarUrlToFirestore(String avatarUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(userId)
                .update("avatarUrl", avatarUrl)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Avatar URL saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to save avatar URL", Toast.LENGTH_SHORT).show());
    }





    private void bindingView() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        navHeaderName = headerView.findViewById(R.id.nav_header_name);
        navHeaderEmail = headerView.findViewById(R.id.nav_header_email);
        imageAvatarView = headerView.findViewById(R.id.imageAvatarView);
        uploadAvatarText = headerView.findViewById(R.id.uploadAvatarText);

        forgotPwdText = headerView.findViewById(R.id.forgotPwd);
        logoutButton = navigationView.findViewById(R.id.logoutButton);
    }
    private void bindingAction() {
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
        // Show password change dialog on forgot password click
        forgotPwdText.setOnClickListener(v -> showChangePasswordDialog());
        // Upload avatar action
        uploadAvatarText.setOnClickListener(v -> openImagePicker());

    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            avatarUri = data.getData();
            displaySelectedImage(avatarUri); // Display the selected image
            uploadAvatarToFirebase();        // Upload to Firebase
        }
    }
    private void displaySelectedImage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            imageAvatarView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showChangePasswordDialog() {
        // Inflate dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        EditText oldPasswordEditText = dialogView.findViewById(R.id.oldPasswordEditText);
        EditText newPasswordEditText = dialogView.findViewById(R.id.newPasswordEditText);
        EditText repeatPasswordEditText = dialogView.findViewById(R.id.repeatPasswordEditText);
        Button changePasswordButton = dialogView.findViewById(R.id.changePasswordButton);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Handle change password logic
        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String repeatPassword = repeatPasswordEditText.getText().toString().trim();

            if (validatePasswordInputs(oldPassword, newPassword, repeatPassword)) {
                changePassword(oldPassword, newPassword, dialog);
            }
        });

        dialog.show();


    }

    private boolean validatePasswordInputs(String oldPassword, String newPassword, String repeatPassword) {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPassword.equals(repeatPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(this, "New password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void changePassword(String oldPassword, String newPassword, AlertDialog dialog) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        // Reauthenticate the user with old password
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update the password
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication failed: Incorrect old password", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void setupNavigation() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_daily_meal, R.id.nav_favourite)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");
                            String userEmail = documentSnapshot.getString("email");
                            String avatarUrl = documentSnapshot.getString("avatarUrl");

                            navHeaderName.setText(userName != null ? userName : "User Name");
                            navHeaderEmail.setText(userEmail != null ? userEmail : "user@example.com");

                            if(avatarUrl != null) {
                                Glide.with(this)
                                        .load(avatarUrl)
                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(48)))
                                        .placeholder(R.drawable.baseline_person_24) // Placeholder image
                                        .into(imageAvatarView);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    );
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });

        initializeFirebase();
        checkUserStatus();
        bindingView();
        bindingAction();
        setupNavigation();
        loadUserData();

//        //Initialize Firebase:
//        FirebaseApp.initializeApp(this);
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null) {
//            // User is not signed in, redirect to LoginActivity
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
//
//        EdgeToEdge.enable(this);
//
//        setContentView(R.layout.activity_main);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//
//        View headerView = navigationView.getHeaderView(0);
//        // Get reference to the TextViews in the header layout
//        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
//        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);
//
//        if (currentUser != null)  {
//            String userId = currentUser.getUid();
//            db.collection("Users").document(userId).get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            // Get name and email from Firestore and set them in the header
//                            String userName = documentSnapshot.getString("name");
//                            String userEmail = documentSnapshot.getString("email");
//
//                            navHeaderName.setText(userName != null ? userName : "User Name");
//                            navHeaderEmail.setText(userEmail != null ? userEmail : "user@example.com");
//                        } else {
//                            Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
//                    });
//
//
//        }
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//            R.id.nav_home, R.id.nav_daily_meal, R.id.nav_favourite)
//            .setOpenableLayout(drawer)
//            .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//        // Find the Log Out button inside the NavigationView
//        Button logoutButton = navigationView.findViewById(R.id.logoutButton);
//        if (logoutButton != null) {
//            logoutButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Sign out the user
//                    mAuth.signOut();
//
//                    // Redirect to LoginActivity
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle navigation when the back button on the toolbar is pressed
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}