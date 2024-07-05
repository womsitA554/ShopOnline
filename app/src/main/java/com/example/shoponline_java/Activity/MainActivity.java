package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoponline_java.Fragment.CartFragment;
import com.example.shoponline_java.Fragment.HomeFragment;
import com.example.shoponline_java.Fragment.ProfileFragment;
import com.example.shoponline_java.Fragment.WishListFragment;
import com.example.shoponline_java.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.onesignal.OneSignal;

public class MainActivity extends BaseActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    WishListFragment wishListFragment;
    CartFragment cartFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        homeFragment = new HomeFragment();
        wishListFragment = new WishListFragment();
        cartFragment = new CartFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, homeFragment).commit();
                }
                if (menuItem.getItemId() == R.id.search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, wishListFragment).commit();
                }
                if (menuItem.getItemId() == R.id.cart) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, cartFragment).commit();
                }
                if (menuItem.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, profileFragment).commit();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
    }
}
