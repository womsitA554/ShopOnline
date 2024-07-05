package com.example.shoponline_java.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline_java.Adapter.OrderCompletedAdapter;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.Order;
import com.example.shoponline_java.R;

import java.util.ArrayList;
import java.util.List;

public class OrderCompletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        OrderCompletedAdapter orderCompletedAdapter;
        ArrayList<Order> orderArrayList = new ArrayList<>();
        FirebaseHelper firebaseHelper;
        String userId;
        ImageView btnBack;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);

        userId = getIntent().getStringExtra("userId");

        firebaseHelper = new FirebaseHelper();

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderCompletedActivity.this, LinearLayoutManager.VERTICAL, false));
        orderCompletedAdapter = new OrderCompletedAdapter(orderArrayList, OrderCompletedActivity.this);
        recyclerView.setAdapter(orderCompletedAdapter);

        firebaseHelper.getAllOrder(userId, new FirebaseHelper.OrderCompletedCallBack() {
            @Override
            public void onCallBack(List<Order> item) {
                if (!item.isEmpty()){
                    orderArrayList.clear();
                    orderArrayList.addAll(item);
                    orderCompletedAdapter.notifyDataSetChanged();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}