package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.models.User;
import com.example.numerology_prm392_group2.service.AppDatabaseService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class registerActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextInputEditText fullNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton signUpButton;
    private View signInText;

    private TextInputLayout fullNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        signInText = findViewById(R.id.signInText);
        fullNameLayout = (TextInputLayout) fullNameEditText.getParent().getParent();
        emailLayout = (TextInputLayout) emailEditText.getParent().getParent();
        passwordLayout = (TextInputLayout) passwordEditText.getParent().getParent();
        confirmPasswordLayout = (TextInputLayout) confirmPasswordEditText.getParent().getParent();

        Log.d(TAG, "Views initialized successfully");
    }

    private void setupClickListeners() {
        signUpButton.setOnClickListener(v -> {
            Log.d(TAG, "Sign up button clicked");
            performRegister();
        });

        signInText.setOnClickListener(v -> {
            Log.d(TAG, "Sign in text clicked");
            Intent intent = new Intent(registerActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegister() {
        Log.d(TAG, "Starting registration process");
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        Log.d(TAG, "Input values - Name: " + fullName + ", Email: " + email + ", Password length: " + password.length());
        if (!validateInput(fullName, email, password, confirmPassword)) {
            Log.d(TAG, "Input validation failed");
            return;
        }
        setLoadingState(true);

        // Lưu vào ROOM DB sau khi kiểm tra trùng email
        new Thread(() -> {
            AppDatabaseService db = AppDatabaseService.getInstance(getApplicationContext());
            User existUser = db.userDao().getUserByEmail(email);
            runOnUiThread(() -> {
                if (existUser != null) {
                    setLoadingState(false);
                    setError(emailLayout, "Email đã được sử dụng");
                    emailEditText.requestFocus();
                } else {
                    saveUserToLocalDatabase(fullName, email, password);
                }
            });
        }).start();
    }

    private void saveUserToLocalDatabase(String fullName, String email, String password) {
        User user = new User(fullName, email, password);
        new Thread(() -> {
            AppDatabaseService db = AppDatabaseService.getInstance(getApplicationContext());
            db.userDao().insert(user);
            Log.d(TAG, "User inserted to Room database: " + email);
            runOnUiThread(() -> {
                setLoadingState(false);
                String successMessage = "Đăng ký thành công! Bạn có thể đăng nhập ngay.";
                showSuccessDialog("Thành công", successMessage, () -> {
                    Intent intent = new Intent(registerActivity.this, Login.class);
                    if (!email.isEmpty()) {
                        intent.putExtra("email", email);
                    }
                    startActivity(intent);
                    finish();
                });
            });
        }).start();
    }


    private boolean validateInput(String fullName, String email, String password, String confirmPassword) {
        boolean isValid = true;

        clearAllErrors();
        if (fullName.isEmpty()) {
            setError(fullNameLayout, "Vui lòng nhập tên người dùng");
            if (isValid) fullNameEditText.requestFocus();
            isValid = false;
        } else if (fullName.length() < 2) {
            setError(fullNameLayout, "Tên người dùng phải có ít nhất 2 ký tự");
            if (isValid) fullNameEditText.requestFocus();
            isValid = false;
        } else if (fullName.length() > 50) {
            setError(fullNameLayout, "Tên người dùng không được quá 50 ký tự");
            if (isValid) fullNameEditText.requestFocus();
            isValid = false;
        }

        if (email.isEmpty()) {
            setError(emailLayout, "Vui lòng nhập email");
            if (isValid) emailEditText.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(emailLayout, "Email không hợp lệ");
            if (isValid) emailEditText.requestFocus();
            isValid = false;
        }

        if (password.isEmpty()) {
            setError(passwordLayout, "Vui lòng nhập mật khẩu");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            setError(passwordLayout, "Mật khẩu phải có ít nhất 6 ký tự");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else if (password.length() > 100) {
            setError(passwordLayout, "Mật khẩu không được quá 100 ký tự");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            setError(confirmPasswordLayout, "Vui lòng xác nhận mật khẩu");
            if (isValid) confirmPasswordEditText.requestFocus();
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            setError(confirmPasswordLayout, "Mật khẩu xác nhận không khớp");
            if (isValid) confirmPasswordEditText.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void clearAllErrors() {
        fullNameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
    }

    private void setError(TextInputLayout layout, String message) {
        layout.setError(message);
    }

    private void showSuccessDialog(String title, String message, Runnable onDismissAction) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    if (onDismissAction != null) {
                        onDismissAction.run();
                    }
                })
                .setCancelable(false);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing() && !isFinishing() && !isDestroyed()) {
                dialog.dismiss();
                if (onDismissAction != null) {
                    onDismissAction.run();
                }
            }
        }, 2000);
    }

    private void setLoadingState(boolean isLoading) {
        runOnUiThread(() -> {
            signUpButton.setEnabled(!isLoading);
            signUpButton.setText(isLoading ? "Đang đăng ký..." : "Đăng Ký");
            fullNameEditText.setEnabled(!isLoading);
            emailEditText.setEnabled(!isLoading);
            passwordEditText.setEnabled(!isLoading);
            confirmPasswordEditText.setEnabled(!isLoading);
            signInText.setEnabled(!isLoading);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RegisterActivity destroyed");
    }
}
