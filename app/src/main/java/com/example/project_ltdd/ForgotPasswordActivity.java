package com.example.project_ltdd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String DEMO_EMAIL = "huynhthaitoan.12c1htk@gmail.com";
    private String generatedOtp;
    EditText userName;
    Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = findViewById(R.id.userName);
        resetPasswordButton = findViewById(R.id.login_button);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userName.getText().toString().trim();

                // Kiểm tra nếu email nhập vào trống
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu email đúng với tài khoản mẫu
                if (email.equals(DEMO_EMAIL)) {
                    // Tạo OTP
                    generatedOtp = generateOtp();
                    // Gửi OTP qua email (giả lập)
                    sendOtpToEmail(email, generatedOtp);
                    Toast.makeText(ForgotPasswordActivity.this, "Mã OTP đã được gửi đến email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOtpToEmail(String email, String otp) {
        new Thread(() -> {
            try {
                // Cấu hình SMTP server
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                // Đăng nhập vào tài khoản Gmail
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("anluc1412@gmail.com", "qzvw sgbu bvkz jwlt");
                    }
                });

                // Tạo email
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("anluc1412@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Mã OTP của bạn");
                message.setText("Mã OTP của bạn là: " + otp);

                // Gửi email
                Transport.send(message);
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Đã gửi OTP qua email!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Lỗi khi gửi email!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
