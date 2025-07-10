package com.example.numerology_prm392_group2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.numerology_prm392_group2.api.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:5005/";
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = "ApiService";

    private static ApiService instance;
    private ApiInterface apiInterface;
    private Context context;

    private ApiService(Context context) {
        this.context = context.getApplicationContext();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Create OkHttp client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    okhttp3.Request originalRequest = chain.request();

                    // Add authorization header if token exists
                    String token = getAuthToken();
                    if (token != null && !token.isEmpty()) {
                        okhttp3.Request newRequest = originalRequest.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .header("Content-Type", "application/json")
                                .build();
                        return chain.proceed(newRequest);
                    }

                    okhttp3.Request newRequest = originalRequest.newBuilder()
                            .header("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Create Gson converter
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Create API interface
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public String getAuthToken() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("auth_token", null);
    }

    public String getUserId() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("user_id", null);
    }

    public String getUserName() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", null);
        Log.d(TAG, "Retrieved username: " + userName);
        return userName;
    }

    public boolean isLoggedIn() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isLoggedInFlag = prefs.getBoolean("is_logged_in", false);
        String token = prefs.getString("auth_token", null);
        String userId = prefs.getString("user_id", null);

        // Return true only if we have all required information
        boolean result = isLoggedInFlag && token != null && !token.isEmpty() && userId != null && !userId.isEmpty();
        Log.d(TAG, "Login status check - isLoggedIn: " + result);
        return result;
    }

    public void saveLoginData(String token, String userId, String userName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        editor.putString("auth_token", token);
        editor.putString("user_id", userId);
        editor.putString("user_name", userName);
        editor.putBoolean("is_logged_in", true);

        boolean success = editor.commit(); // Use commit() for immediate write
        Log.d(TAG, "Login data saved successfully: " + success);
        Log.d(TAG, "Saved - Token: " + (token != null ? "present" : "null") +
                ", UserID: " + userId + ", UserName: " + userName);

        // Verify saved data
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedUserName = prefs.getString("user_name", "NOT_FOUND");
        Log.d(TAG, "Verification - Saved username: " + savedUserName);
    }

    /**
     * Clear all login data - used for logout
     */
    public void clearLoginData() {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        // Clear login session data
        editor.remove("auth_token");
        editor.remove("user_id");
        editor.remove("user_name");
        editor.putBoolean("is_logged_in", false);

        // Note: We don't clear saved credentials here so "Remember Me" still works
        // Only clear them if user explicitly wants to forget credentials

        boolean success = editor.commit();
        Log.d(TAG, "Login data cleared successfully: " + success);
    }

    /**
     * Clear all data including saved credentials - complete logout
     */
    public void clearAllData() {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.clear(); // Clear everything
        boolean success = editor.commit();
        Log.d(TAG, "All data cleared successfully: " + success);
    }

    public void saveCredentials(String email, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        if (rememberMe) {
            editor.putString("saved_email", email);
            editor.putString("saved_password", password);
            editor.putBoolean("remember_me", true);
            Log.d(TAG, "Credentials saved for email: " + email);
        } else {
            editor.remove("saved_email");
            editor.remove("saved_password");
            editor.putBoolean("remember_me", false);
            Log.d(TAG, "Credentials cleared");
        }
        editor.apply();
    }

    public String[] getSavedCredentials() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (prefs.getBoolean("remember_me", false)) {
            String[] credentials = new String[]{
                    prefs.getString("saved_email", ""),
                    prefs.getString("saved_password", "")
            };
            Log.d(TAG, "Retrieved saved credentials for: " + credentials[0]);
            return credentials;
        }
        return new String[]{"", ""};
    }

    public boolean isRememberMeEnabled() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean rememberMe = prefs.getBoolean("remember_me", false);
        Log.d(TAG, "Remember Me enabled: " + rememberMe);
        return rememberMe;
    }

    /**
     * Get current user info as a formatted string
     */
    public String getCurrentUserInfo() {
        if (!isLoggedIn()) {
            return "Not logged in";
        }

        return String.format("User: %s (ID: %s)",
                getUserName() != null ? getUserName() : "Unknown",
                getUserId() != null ? getUserId() : "Unknown");
    }

    /**
     * Check if current token is valid (basic check)
     */
    public boolean isTokenValid() {
        String token = getAuthToken();
        return token != null && !token.isEmpty() && token.length() > 10; // Basic validation
    }
}