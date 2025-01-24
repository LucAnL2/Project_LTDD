package com.example.project_ltdd.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_ltdd.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.project_ltdd.api.ApiService;
import com.example.project_ltdd.api.RetrofitClient;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(email, password);
        });
    }

    private void registerUser(String email, String password) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        // Gửi yêu cầu đăng ký
        apiService.register(body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Đăng ký thành công, gửi OTP qua email
                    sendOtpToEmail(email);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendOtpToEmail(String email) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Gửi OTP qua email
        apiService.sendOtp(email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "OTP sent to email!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to send OTP!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
