package com.example.numerology_prm392_group2.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.numerology_prm392_group2.models.ErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

public class NetworkUtils {

    /**
     * Check if device has internet connection
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * Parse error response from API
     */
    public static String parseErrorResponse(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Gson gson = new Gson();
                ErrorResponse errorResponse = gson.fromJson(errorBody, ErrorResponse.class);

                if (errorResponse != null && errorResponse.getMessage() != null) {
                    return errorResponse.getMessage();
                }

                return errorBody;
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        return getDefaultErrorMessage(response.code());
    }

    /**
     * Get default error message based on HTTP status code
     */
    public static String getDefaultErrorMessage(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Yêu cầu không hợp lệ";
            case 401:
                return "Không có quyền truy cập";
            case 403:
                return "Truy cập bị từ chối";
            case 404:
                return "Không tìm thấy tài nguyên";
            case 408:
                return "Timeout kết nối";
            case 500:
                return "Lỗi server nội bộ";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Dịch vụ tạm thời không khả dụng";
            case 504:
                return "Gateway Timeout";
            default:
                return "Đã xảy ra lỗi không xác định";
        }
    }

    /**
     * Check if error is network related
     */
    public static boolean isNetworkError(Throwable throwable) {
        return throwable instanceof java.net.UnknownHostException ||
                throwable instanceof java.net.ConnectException ||
                throwable instanceof java.net.SocketTimeoutException ||
                throwable instanceof java.net.SocketException;
    }

    /**
     * Get user-friendly error message from throwable
     */
    public static String getErrorMessage(Throwable throwable) {
        if (throwable == null) {
            return "Đã xảy ra lỗi không xác định";
        }

        if (throwable instanceof java.net.UnknownHostException) {
            return "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.";
        } else if (throwable instanceof java.net.ConnectException) {
            return "Không thể kết nối đến server";
        } else if (throwable instanceof java.net.SocketTimeoutException) {
            return "Kết nối bị timeout. Vui lòng thử lại.";
        } else if (throwable instanceof java.net.SocketException) {
            return "Lỗi kết nối mạng";
        } else {
            String message = throwable.getMessage();
            return message != null ? message : "Đã xảy ra lỗi không xác định";
        }
    }
}