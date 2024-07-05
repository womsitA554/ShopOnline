package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline_java.Adapter.OrderAdapter;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView tvOrderId, tvPrice, tvAddress, tvOrderTime, tvPaymentMethod;
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPrice = findViewById(R.id.tvPrice);
        tvAddress = findViewById(R.id.tvAddress);
        tvOrderTime = findViewById(R.id.tvTime);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);

        recyclerView = findViewById(R.id.recyclerView);


        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");
        double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);
        Log.d("getTotalPrice",String.valueOf(totalPrice));
        String address = intent.getStringExtra("address");
        String orderTime = intent.getStringExtra("orderTime");
        String orderPayment = intent.getStringExtra("orderPayment");
        List<Item> orderItems = (List<Item>) intent.getSerializableExtra("cartItems");

        tvPrice.setText("$" + totalPrice);
        tvAddress.setText(address);
        tvOrderTime.setText(orderTime);
        tvPaymentMethod.setText(orderPayment);

        orderAdapter = new OrderAdapter(orderItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderAdapter);

    }
}