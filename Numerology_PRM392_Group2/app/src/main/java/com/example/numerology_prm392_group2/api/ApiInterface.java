package com.example.numerology_prm392_group2.api;

import com.example.numerology_prm392_group2.models.BetListResponse;
import com.example.numerology_prm392_group2.models.BetRequest;
import com.example.numerology_prm392_group2.models.BetResponse;
import com.example.numerology_prm392_group2.models.LoginRequest;
import com.example.numerology_prm392_group2.models.LoginResponse;
import com.example.numerology_prm392_group2.models.RegisterRequest;
import com.example.numerology_prm392_group2.models.RegisterResponse;
import com.example.numerology_prm392_group2.models.UpdateProfileRequest;
import com.example.numerology_prm392_group2.models.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface ApiInterface {


    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET("api/user/profile")
    Call<UserProfileResponse> getUserProfile();


    @PUT("api/user/profile/update")
    Call<Void> updateUserProfile(@Body UpdateProfileRequest updateProfile);


    @POST("api/auth/forgot-password")
    Call<LoginResponse> forgotPassword(@Body LoginRequest emailRequest);

    @POST("api/auth/refresh-token")
    Call<LoginResponse> refreshToken(@Header("Authorization") String token);

    @POST("api/auth/logout")
    Call<LoginResponse> logout(@Header("Authorization") String token);


    @POST("api/bets")
    Call<BetResponse> createBet(@Body BetRequest betRequest);

    @GET("api/bets")
    Call<BetListResponse> getUserBets();


    @GET("api/bets")
    Call<BetListResponse> getUserBets(@Query("page") int page, @Query("size") int size);


    @GET("api/bets/date")
    Call<BetListResponse> getBetsByDate(@Query("date") String date);


    @GET("api/bets/{id}")
    Call<BetResponse> getBetById(@Path("id") String betId);


    @GET("api/bets/number/{number}")
    Call<BetListResponse> getBetsByNumber(@Path("number") int number);


    @POST("api/bets/{id}/delete")
    Call<BetResponse> deleteBet(@Path("id") String betId);


    @POST("api/bets/{id}/update")
    Call<BetResponse> updateBet(@Path("id") String betId, @Body BetRequest betRequest);


    @GET("api/bets/statistics")
    Call<BetListResponse> getBetStatistics();


    @GET("api/bets/statistics/range")
    Call<BetListResponse> getBetStatistics(@Query("startDate") String startDate, @Query("endDate") String endDate);
}