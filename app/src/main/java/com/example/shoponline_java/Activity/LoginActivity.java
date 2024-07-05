package com.example.shoponline_java.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.User;
import com.example.shoponline_java.R;

public class LoginActivity extends BaseActivity {
    TextView btnSignUp;
    EditText etPhoneNumber, etPassword;
    AppCompatButton btnLogin;
    FirebaseHelper firebaseHelper;
    private SharedPreferences sharedPreferences;
    String userId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("loginSave", Context.MODE_PRIVATE);

        firebaseHelper = new FirebaseHelper();
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        etPhoneNumber = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneTxt = etPhoneNumber.getText().toString();
                String passwordTxt = etPassword.getText().toString();
                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    checkPhoneNumber(phoneTxt, passwordTxt);
                }
            }
        });
    }

    private void checkPhoneNumber(String phoneNumber, String password) {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.getPhoneNumberAndPassword(phoneNumber, new FirebaseHelper.UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
                    firebaseHelper.getUserId(phoneNumber, new FirebaseHelper.UserIdCallback() {
                        @Override
                        public void onCallback(String userId) {
                            if (userId != null) {
                                // save login pref
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", userId);
                                editor.putBoolean("isLogin", true);
                                editor.apply();
                                // userId được tìm thấy, chuyển sang MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userId", userId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to get userId", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}