package com.example.shoponline_java.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.shoponline_java.Activity.CheckOutActivity;
import com.example.shoponline_java.Adapter.CartAdapter;
import com.example.shoponline_java.Helper.ChangeNumberItemsListener;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartEmptyListener {
    TextView tvSubTotal, tvTotalTax, tvTotal, tv1, tvSize;
    double tax;
    FirebaseHelper firebaseHelper;
    RecyclerView recyclerViewCart;
    AppCompatButton btnApply, btnCheckOut, btnExplore;
    ScrollView scrollView;
    CartAdapter cartAdapter;
    String userId;
    ImageView iconSearch;
    ArrayList<Item> items = new ArrayList<>();
    AppCompatButton getBtnCheckOut;
    double total = 0.0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            Log.d("check1", userId);
        }

        firebaseHelper = new FirebaseHelper();
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvTotalTax = view.findViewById(R.id.tvTotalTax);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnApply = view.findViewById(R.id.btnApply);
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        btnCheckOut = view.findViewById(R.id.btnCheckOut);
        scrollView = view.findViewById(R.id.scrollView);
        btnExplore = view.findViewById(R.id.btnExplore);
        tv1 = view.findViewById(R.id.text1);
        btnCheckOut = view.findViewById(R.id.btnCheckOut);

        btnExplore.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_frameLayout, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        btnCheckOut.setOnClickListener(v -> {
            Intent intent1 = new Intent(getActivity(), CheckOutActivity.class);
            intent1.putExtra("userId", userId);
            intent1.putExtra("total", total);
            startActivity(intent1);
        });

        calculatorCart();

        return view;
    }

    private void setUpRecycleViewCart() {
        if (userId != null) {
            firebaseHelper.getAllItemInCart(userId, new FirebaseHelper.CartCallBack() {
                @Override
                public void onCallBack(List<Item> list) {
                    if (!list.isEmpty()) {
                        tv1.setVisibility(View.GONE);
                        btnExplore.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);

                        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        cartAdapter = new CartAdapter((ArrayList<Item>) list, getContext(), userId, new ChangeNumberItemsListener() {
                            @Override
                            public void changed() {
                                calculatorCart();
                            }
                        }, CartFragment.this); // Pass the listener
                        recyclerViewCart.setAdapter(cartAdapter);
                        calculatorCart();
                    } else {
                        tv1.setVisibility(View.VISIBLE);
                        btnExplore.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            // If userId is null, clear the cart and update UI accordingly
            // Update UI to show empty cart state
            tv1.setVisibility(View.VISIBLE);
            btnExplore.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpRecycleViewCart();
        calculatorCart();
    }

    private void calculatorCart() {
        if (cartAdapter == null) {
            return;
        }
        items.clear();
        items.addAll(cartAdapter.getAllItemInCart());

        double totalItem = 0.0;
        for (Item item : items) {
            int quantityOfItem = item.getNumberInCart();
            double priceOfItem = item.getPrice();

            double subTotalOfEachItem = Double.parseDouble(String.valueOf(quantityOfItem)) * priceOfItem;
            totalItem += subTotalOfEachItem;
        }

        double tax = 0.05;
        tax = totalItem * tax;
        total = totalItem + tax;

        tvSubTotal.setText("$" + totalItem);
        tvTotalTax.setText("$" + tax);
        tvTotal.setText("$" + total);
    }

    @Override
    public void onCartEmpty() {
        // Reset the CartFragment or show a message
        getFragmentManager().beginTransaction().replace(R.id.main_frameLayout, new CartFragment()).commit();
    }
}