package com.example.numerology_prm392_group2.service;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static volatile Retrofit retrofit;

    private static final String BASE_URL = "https://raw.githubusercontent.com/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    retrofit = createRetrofit();
                }
            }
        }
        return retrofit;
    }

    private static Retrofit createRetrofit() {
        // Logging interceptor để debug
        var loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Tạo OkHttpClient (compatible với tất cả Android versions)
        var httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    var original = chain.request();
                    var request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("User-Agent", "NumerologyApp-Android/1.0")
                            .header("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)  // Dùng TimeUnit thay vì Duration
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                // Cách 1: Dùng BuildConfig (cần enable buildFeatures.buildConfig = true)
                //.baseUrl(BuildConfig.API_BASE_URL)

                // Cách 2: Dùng constant (nếu BuildConfig không work)
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}