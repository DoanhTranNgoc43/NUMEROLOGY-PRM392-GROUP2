package com.example.numerology_prm392_group2.api;

import com.example.numerology_prm392_group2.models.BetListResponse;
import com.example.numerology_prm392_group2.models.BetRequest;
import com.example.numerology_prm392_group2.models.BetResponse;
import com.example.numerology_prm392_group2.models.LoginRequest;
import com.example.numerology_prm392_group2.models.LoginResponse;
import com.example.numerology_prm392_group2.models.RegisterRequest;
import com.example.numerology_prm392_group2.models.RegisterResponse;
import com.example.numerology_prm392_group2.models.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiInterface {

    // ====== AUTHENTICATION ENDPOINTS ======
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("api/user/profile")
    Call<UserProfileResponse> getUserProfile();

    // Updated to use the correct request model
    @POST("api/user/profile/update")
    Call<Void> updateUserProfile(@Body UserProfileResponse user);


    @POST("api/auth/forgot-password")
    Call<LoginResponse> forgotPassword(@Body LoginRequest emailRequest);

    @POST("api/auth/refresh-token")
    Call<LoginResponse> refreshToken(@Header("Authorization") String token);

    @POST("api/auth/logout")
    Call<LoginResponse> logout(@Header("Authorization") String token);

    // ====== BET ENDPOINTS ======

    // Create a new bet
    @POST("api/bets")
    Call<BetResponse> createBet(@Body BetRequest betRequest);

    // Get all bets for current user
    @GET("api/bets")
    Call<BetListResponse> getUserBets();

    // Get all bets for current user with pagination
    @GET("api/bets")
    Call<BetListResponse> getUserBets(@Query("page") int page, @Query("size") int size);

    // Get bets by date
    @GET("api/bets/date")
    Call<BetListResponse> getBetsByDate(@Query("date") String date);

    // Get bet by ID
    @GET("api/bets/{id}")
    Call<BetResponse> getBetById(@Path("id") String betId);

    // Get bets by number
    @GET("api/bets/number/{number}")
    Call<BetListResponse> getBetsByNumber(@Path("number") int number);

    // Delete a bet
    @POST("api/bets/{id}/delete")
    Call<BetResponse> deleteBet(@Path("id") String betId);

    // Update a bet
    @POST("api/bets/{id}/update")
    Call<BetResponse> updateBet(@Path("id") String betId, @Body BetRequest betRequest);

    // Get bet statistics
    @GET("api/bets/statistics")
    Call<BetListResponse> getBetStatistics();

    // Get bet statistics by date range
    @GET("api/bets/statistics/range")
    Call<BetListResponse> getBetStatistics(@Query("startDate") String startDate, @Query("endDate") String endDate);
}