package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.CheckBox;

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

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private CheckBox rememberMeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        loadSavedCredentials();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> performLogin());
        findViewById(R.id.forgotPasswordText).setOnClickListener(v -> {
            showAlertDialog("Thông báo", "Chức năng quên mật khẩu đang được phát triển");
        });
        findViewById(R.id.signUpText).setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, registerActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.googleIcon).setOnClickListener(v -> {
            showAlertDialog("Thông báo", "Đăng nhập bằng Google đang được phát triển");
        });
        findViewById(R.id.facebookIcon).setOnClickListener(v -> {
            showAlertDialog("Thông báo", "Đăng nhập bằng Facebook đang được phát triển");
        });
        findViewById(R.id.instagramIcon).setOnClickListener(v -> {
            showAlertDialog("Thông báo", "Đăng nhập bằng Instagram đang được phát triển");
        });
    }

    private void loadSavedCredentials() {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String savedEmail = prefs.getString("email", "");
        String savedPassword = prefs.getString("password", "");
        boolean remember = prefs.getBoolean("remember_me", false);

        if (remember) {
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
            rememberMeCheckbox.setChecked(true);
        }
    }

    private void saveCredentials(String email, String password, boolean rememberMe) {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (rememberMe) {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("remember_me", true);
        } else {
            editor.remove("email");
            editor.remove("password");
            editor.putBoolean("remember_me", false);
        }
        editor.apply();
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        setLoadingState(true);

        new Thread(() -> {
            AppDatabaseService db = AppDatabaseService.getInstance(getApplicationContext());
            User user = db.userDao().getUserByEmail(email);
            runOnUiThread(() -> {
                setLoadingState(false);
                if(user == null) {
                    emailEditText.setError("Tài khoản không tồn tại");
                    showAlertDialog("Lỗi", "Email chưa được đăng ký.");
                }
                else if(!user.password.equals(password)) {
                    passwordEditText.setError("Sai mật khẩu");
                    showAlertDialog("Lỗi", "Mật khẩu không đúng.");
                }
                else {
                    saveLoginStatus(user.fullName, user.email);
                    saveCredentials(email, password, rememberMeCheckbox.isChecked());
                    showAutoDismissDialog(
                            "Xin chào, " + user.fullName,
                            this::navigateToMainActivity
                    );
                }
            });
        }).start();
    }

    private void saveLoginStatus(String fullName, String email) {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("user_name", fullName);
        editor.putString("user_email", email);
        editor.apply();
    }

    public static void clearLoginStatus(AppCompatActivity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", false);
        editor.remove("user_name");
        editor.remove("user_email");
        editor.apply();
    }


    private boolean validateInput(String email, String password) {
        boolean isValid = true;

        if (email.isEmpty()) {
            emailEditText.setError("Vui lòng nhập tài khoản");
            emailEditText.requestFocus();
            isValid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Vui lòng nhập mật khẩu");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            if (isValid) passwordEditText.requestFocus();
            isValid = false;
        } else {
            passwordEditText.setError(null);
        }

        return isValid;
    }

    private void showAlertDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showAutoDismissDialog(String message, Runnable onDismissAction) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Đăng nhập thành công")
                .setMessage(message);

        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                if (onDismissAction != null) {
                    onDismissAction.run();
                }
            }
        }, 1500);
    }

    private void setLoadingState(boolean isLoading) {
        loginButton.setEnabled(!isLoading);
        loginButton.setText(isLoading ? "Đang đăng nhập..." : "Đăng Nhập");
        emailEditText.setEnabled(!isLoading);
        passwordEditText.setEnabled(!isLoading);
        rememberMeCheckbox.setEnabled(!isLoading);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeSubAgentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
