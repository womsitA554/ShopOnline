package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoponline_java.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    String phoneNumber;
    TextView btnSignIn;
    String verificationCode;
    AppCompatButton btnCheck, btnGetOTP;
    EditText etInternational, etPhone, etCode;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();
        etInternational = findViewById(R.id.etInternational);
        etPhone = findViewById(R.id.etPhone);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etCode = findViewById(R.id.etCode);
        btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etCode.getText().toString();
                if (otp.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    verifyOtp(otp);
                }
            }
        });

        btnGetOTP = findViewById(R.id.btnGetOTP);
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String internationalTxt = etInternational.getText().toString();
                String phoneTxt = etPhone.getText().toString();
                phoneNumber = internationalTxt + phoneTxt;
                if (internationalTxt.isEmpty() || phoneTxt.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    sendOtp(phoneNumber, false);
                }
            }
        });

    }

    void sendOtp(String phoneNumber, boolean isResend) {
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signUpPassword(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(SignUpActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationCode = s;
                        resendingToken = forceResendingToken;
                        Toast.makeText(SignUpActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                    }


                });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void verifyOtp(String otp) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, otp);
        signUpPassword(phoneAuthCredential);
    }

    void signUpPassword(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUpActivity.this, SignUpPasswordActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}