package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numerology_prm392_group2.R;

import com.example.numerology_prm392_group2.models.LoginRequest;
import com.example.numerology_prm392_group2.models.LoginResponse;
import com.example.numerology_prm392_group2.utils.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private CheckBox rememberMeCheckbox;

    private ApiService apiService;

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
        checkUserLoginStatus();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        apiService = ApiService.getInstance(this);
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

    private void checkUserLoginStatus() {
        if (apiService.isLoggedIn()) {
            navigateToMainActivity();
        }
    }

    private void loadSavedCredentials() {
        if (apiService.isRememberMeEnabled()) {
            String[] credentials = apiService.getSavedCredentials();
            emailEditText.setText(credentials[0]);
            passwordEditText.setText(credentials[1]);
            rememberMeCheckbox.setChecked(true);
        }
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (!validateInput(email, password)) {
            return;
        }
        setLoadingState(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Call API
        Call<LoginResponse> call = apiService.getApiInterface().login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setLoadingState(false);

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body(), email, password);
                } else {
                    handleLoginError(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoadingState(false);
                handleNetworkError(t);
                Log.e(TAG, "Login failed", t);
            }
        });
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

    private void handleLoginSuccess(LoginResponse response, String email, String password) {
        if (response.isSuccess()) {
            String token = response.getToken();
            String userId = "";
            String userName = "";

            if (response.getData() != null && response.getData().getUser() != null) {
                userId = response.getData().getUser().getId();
                userName = response.getData().getUser().getName();
                Log.d(TAG, "Saving username: " + userName);
            } else {
                Log.e(TAG, "User data is null in LoginResponse");
            }
            SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("token", token);
            editor.putString("user_id", userId);
            editor.putString("user_name", userName);
            editor.apply();
            apiService.saveLoginData(token, userId, userName);
            apiService.saveCredentials(email, password, rememberMeCheckbox.isChecked());

            showAutoDismissDialog("Thành công", "Đăng nhập thành công!", this::navigateToMainActivity);
        } else {
            String errorMessage = response.getMessage();
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = "Đăng nhập thất bại";
            }
            showAlertDialog("Lỗi", errorMessage);
        }
    }

    private void handleLoginError(int statusCode, String message) {
        String errorMessage = "Đăng nhập thất bại";

        switch (statusCode) {
            case 401:
                errorMessage = "Thông tin đăng nhập không hợp lệ";
                break;
            case 400:
                errorMessage = "Tài khoản hoặc mật khẩu không đúng";
                break;
            case 403:
                errorMessage = "Tài khoản đã bị khóa";
                break;
            case 404:
                errorMessage = "Tài khoản không tồn tại";
                break;
            case 500:
                errorMessage = "Lỗi server, vui lòng thử lại sau";
                break;
            default:
                if (message != null && !message.isEmpty()) {
                    errorMessage = message;
                }
                break;
        }

        showAlertDialog("Lỗi", errorMessage);
        Log.e(TAG, "Login error: " + statusCode + " - " + message);
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage = "Lỗi kết nối mạng";

        if (t.getMessage() != null) {
            if (t.getMessage().contains("timeout")) {
                errorMessage = "Kết nối bị timeout, vui lòng thử lại";
            } else if (t.getMessage().contains("Unable to resolve host")) {
                errorMessage = "Không thể kết nối đến server";
            }
        }

        showAlertDialog("Lỗi", errorMessage);
    }

    private void showAlertDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showAutoDismissDialog(String title, String message, Runnable onDismissAction) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
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
        }, 2000);
    }

    private void setLoadingState(boolean isLoading) {
        loginButton.setEnabled(!isLoading);
        loginButton.setText(isLoading ? "Đang đăng nhập..." : "Đăng Nhập");
        emailEditText.setEnabled(!isLoading);
        passwordEditText.setEnabled(!isLoading);
        rememberMeCheckbox.setEnabled(!isLoading);
    }

    private void navigateToMainActivity() {
        Log.d(TAG, "Attempting to navigate to HomesubAgentActivity");
        Intent intent = new Intent(getApplicationContext(), HomeSubAgentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.d(TAG, "Navigation intent started");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}