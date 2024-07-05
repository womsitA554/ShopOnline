package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline_java.Adapter.AddressAdapter;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.Address;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.Model.Order;
import com.example.shoponline_java.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {
    ImageView btnBack;
    AddressAdapter adapter;
    RecyclerView recyclerViewAddress;
    ArrayList<Address> list = new ArrayList<>();
    String userId;
    double total = 0.0;
    TextView totalCheckOut, tvNewAddress;
    FirebaseHelper firebaseHelper;
    AppCompatButton btnComplete;
    ArrayList<Item> itemArrayList = new ArrayList<>();
    RadioGroup radioGroup;
    RadioButton rdoBtnGPay, rdoBtnDebit, rdoBtnPaypal, rdoBtnBank;
    String getTextOfRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setListeners();

        userId = getIntent().getStringExtra("userId");
        total = getIntent().getDoubleExtra("total", 0.0);
        Log.d("total", String.valueOf(total));

        setupRecyclerView();
        loadAddressesIfNeeded();

        totalCheckOut.setText("$" + total);
    }

    private void initializeViews() {
        totalCheckOut = findViewById(R.id.totalCheckOut);
        btnBack = findViewById(R.id.btnBack);
        tvNewAddress = findViewById(R.id.tvNewAddress);
        firebaseHelper = new FirebaseHelper();
        recyclerViewAddress = findViewById(R.id.recyclerViewAddress);
        btnComplete = findViewById(R.id.btnComplete);
        radioGroup = findViewById(R.id.radioGroup);
        rdoBtnBank = findViewById(R.id.rdoBtnBank);
        rdoBtnGPay = findViewById(R.id.rdoBtnGPay);
        rdoBtnDebit = findViewById(R.id.rdoBtnDebit);
        rdoBtnPaypal = findViewById(R.id.rdoBtnPaypal);
    }

    private void setListeners() {
        rdoBtnBank.setOnCheckedChangeListener(listenerRadio);
        rdoBtnGPay.setOnCheckedChangeListener(listenerRadio);
        rdoBtnDebit.setOnCheckedChangeListener(listenerRadio);
        rdoBtnPaypal.setOnCheckedChangeListener(listenerRadio);

        btnComplete.setOnClickListener(v -> createOrderAndGenerateInvoice());

        tvNewAddress.setOnClickListener(v -> {
            Intent intent = new Intent(CheckOutActivity.this, AddressActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new AddressAdapter(list, this);
        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAddress.setAdapter(adapter);
    }

    private void loadAddressesIfNeeded() {
        if (userId != null) {
            loadAddresses();
        }
    }

    private void loadAddresses() {
        firebaseHelper.getAllAddress(userId, new FirebaseHelper.AddressCallBack() {
            @Override
            public void onCallBack(List<Address> item) {
                if (item != null && !item.isEmpty()) {
                    list.clear();
                    list.addAll(item);
                    adapter.notifyDataSetChanged();
                    recyclerViewAddress.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewAddress.setVisibility(View.GONE);
                    Toast.makeText(CheckOutActivity.this, "No addresses found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (userId != null) {
            loadAddresses();
        }
    }

    private void createOrderAndGenerateInvoice() {
        if (getTextOfRadioButton == null || getTextOfRadioButton.isEmpty()) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        Address selectedAddress = adapter.getSelectedAddress();
        if (selectedAddress == null) {
            Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseHelper.getAllItemInCart(userId, new FirebaseHelper.CartCallBack() {
            @Override
            public void onCallBack(List<Item> items) {
                if (!items.isEmpty()) {
                    itemArrayList.clear();
                    itemArrayList.addAll(items);

                    String orderTime = getCurrentTime();
                    double totalPrice = total;

                    Order order = new Order(null, userId, itemArrayList, getTextOfRadioButton, selectedAddress.getAddress(), orderTime, totalPrice);

                    firebaseHelper.addOrder(userId, itemArrayList, getTextOfRadioButton, selectedAddress.getAddress(), orderTime, totalPrice, new FirebaseHelper.OrderCallback() {
                        @Override
                        public void onComplete(boolean isSuccess) {
                            if (isSuccess) {
                                navigateToDoneActivity(order);
                            } else {
                                Toast.makeText(CheckOutActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CheckOutActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToDoneActivity(Order order) {
        Intent intent = new Intent(CheckOutActivity.this, DoneActivity.class);
        intent.putExtra("order", order);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    CompoundButton.OnCheckedChangeListener listenerRadio = (buttonView, isChecked) -> {
        if (isChecked) {
            getTextOfRadioButton = buttonView.getText().toString();
        }
    };
}
