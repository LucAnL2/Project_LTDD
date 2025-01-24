package com.example.project_ltdd.api;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/auth/register")
    Call<Void> register(@Body Map<String, String> body);

    // Thêm phương thức gửi OTP nếu cần
    @POST("/api/auth/send-otp")
    Call<Void> sendOtp(@Body String email);
}

