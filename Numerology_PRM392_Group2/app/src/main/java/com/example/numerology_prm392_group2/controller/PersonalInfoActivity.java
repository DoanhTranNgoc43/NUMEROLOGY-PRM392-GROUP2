package com.example.numerology_prm392_group2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.numerology_prm392_group2.R;
import com.example.numerology_prm392_group2.models.LoginResponse;
import com.example.numerology_prm392_group2.models.UpdateProfileRequest;
import com.example.numerology_prm392_group2.models.UserProfileResponse;
import com.example.numerology_prm392_group2.utils.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity {

    private static final String TAG = "PersonalInfoActivity";
    private ImageView profileImage;
    private EditText editTextFullName, editTextEmail, editTextPhoneNumber;
    private Button buttonSave;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        apiService = ApiService.getInstance(this);

        profileImage = findViewById(R.id.profileImage);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSave = findViewById(R.id.buttonSave);

        loadUserInfoFromAPI();

        buttonSave.setOnClickListener(v -> saveUserInfo());

    }

    private void loadUserInfoFromAPI() {
        loadUserInfoFromPrefs();
        if (!apiService.isLoggedIn()) {
            Log.e(TAG, "User not logged in");
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Making API call with token: " + (apiService.getAuthToken() != null ? "present" : "null"));
        Log.d(TAG, "User ID: " + apiService.getUserId());
        Log.d(TAG, "Base URL: " + apiService.getBaseUrl());

        Call<UserProfileResponse> call = apiService.getApiInterface().getUserProfile();
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                Log.d(TAG, "API Response Code: " + response.code());
                Log.d(TAG, "API Response Message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse userProfile = response.body();

                    Log.d(TAG, "User profile received: " + userProfile.toString());

                    editTextFullName.setText(userProfile.getFullName());
                    editTextEmail.setText(userProfile.getEmail());
                    editTextPhoneNumber.setText(userProfile.getPhoneNumber());

                    saveUserInfoToPrefs(userProfile);

                } else {
                    Log.e(TAG, "Failed to load user profile: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e(TAG, "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }

                    String errorMessage = "Không thể tải thông tin người dùng";
                    if (response.code() == 401) {
                        errorMessage = "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.";
                    } else if (response.code() == 403) {
                        errorMessage = "Không có quyền truy cập thông tin này.";
                    } else if (response.code() == 404) {
                        errorMessage = "Không tìm thấy thông tin người dùng.";
                    } else if (response.code() >= 500) {
                        errorMessage = "Lỗi server. Vui lòng thử lại sau.";
                    }

                    Toast.makeText(PersonalInfoActivity.this, errorMessage + " (Code: " + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e(TAG, "API call failed", t);

                String errorMessage = "Lỗi kết nối";
                if (t instanceof java.net.UnknownHostException) {
                    errorMessage = "Không thể kết nối đến server. Kiểm tra kết nối mạng.";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    errorMessage = "Kết nối quá thời gian chờ. Thử lại sau.";
                } else if (t instanceof java.net.ConnectException) {
                    errorMessage = "Không thể kết nối đến server. Server có thể đang bảo trì.";
                }

                Toast.makeText(PersonalInfoActivity.this, errorMessage + ": " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserInfoFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String fullName = prefs.getString("full_name", "");
        String email = prefs.getString("user_email", "");
        String phoneNumber = prefs.getString("phone_number", "");
        String avatarUrl = prefs.getString("avatarUrl", "");

        editTextFullName.setText(fullName);
        editTextEmail.setText(email);
        editTextPhoneNumber.setText(phoneNumber);

        if (!avatarUrl.isEmpty()) {
            Glide.with(this).load(avatarUrl).into(profileImage);
        }
    }

    private void saveUserInfoToPrefs(UserProfileResponse userProfile) {
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("full_name", userProfile.getFullName());
        editor.putString("user_email", userProfile.getEmail());
        editor.putString("phone_number", userProfile.getPhoneNumber());
        editor.putString("user_id", userProfile.getUserId());
        editor.apply();
    }

    private void saveUserInfo() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.isEmpty() || !android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateProfileRequest updateProfileRequest= new UpdateProfileRequest(fullName, email, phoneNumber);
        Call<Void> call = apiService.getApiInterface().updateUserProfile(updateProfileRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("full_name", fullName);
                    editor.putString("user_email", email);
                    editor.putString("phone_number", phoneNumber);
                    editor.apply();

                    Toast.makeText(PersonalInfoActivity.this, "Đã cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PersonalInfoActivity.this, HomeSubAgentActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = "Cập nhật thông tin thất bại";
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                        if (response.code() == 400 && errorBody.contains("Email is already in use")) {
                            errorMessage = "Email đã được sử dụng.";
                        } else if (response.code() == 401) {
                            errorMessage = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.";
                        } else if (response.code() == 404) {
                            errorMessage = "Không tìm thấy người dùng.";
                        }
                        Log.e(TAG, "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(PersonalInfoActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                Toast.makeText(PersonalInfoActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}