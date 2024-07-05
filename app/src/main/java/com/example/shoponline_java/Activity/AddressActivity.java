package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.Address;
import com.example.shoponline_java.R;

public class AddressActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText etEnterAddress;
    AppCompatButton btnAdd;
    FirebaseHelper firebaseHelper;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getStringExtra("userId");

        firebaseHelper = new FirebaseHelper();

        btnBack = findViewById(R.id.btnBack);
        etEnterAddress = findViewById(R.id.etEnterAddress);
        btnAdd = findViewById(R.id.btnAdd);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressText = etEnterAddress.getText().toString();
                if (!addressText.isEmpty()) {
                    firebaseHelper.addAddress(userId, addressText);
                    Toast.makeText(AddressActivity.this, "Add successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddressActivity.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}