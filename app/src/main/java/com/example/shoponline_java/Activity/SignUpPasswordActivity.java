package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.R;

public class SignUpPasswordActivity extends AppCompatActivity {
    EditText etPassword, etRepeatPassword;
    String phoneNumber;
    AppCompatButton btnSignUp;
    FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseHelper = new FirebaseHelper();

        etPassword = findViewById(R.id.etPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordTxt = etPassword.getText().toString();
                String repeatPasswordTxt = etRepeatPassword.getText().toString();

                if (passwordTxt.isEmpty() || repeatPasswordTxt.isEmpty()){
                    Toast.makeText(SignUpPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                else if (!passwordTxt.equals(repeatPasswordTxt)){
                    Toast.makeText(SignUpPasswordActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseHelper.addUsers(firebaseHelper.getCurrentId(), phoneNumber, passwordTxt);
                    finish();
                    etPassword.setText("");
                    etRepeatPassword.setText("");
                }
            }
        });
    }
}