package com.example.shoponline_java.Activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoponline_java.R;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("dark_theme", false)) {
            setTheme(R.style.Base_Theme_ShopOnline_Java_Dark);
        } else {
            setTheme(R.style.Base_Theme_ShopOnline_Java);
        }
        super.onCreate(savedInstanceState);

        applyLanguage();
        applyNotification();
    }

    private void applyLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = sharedPreferences.getString("My_Lang", "en");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Log.d("checkLangInBase", "changeLanguage: " + lang);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void applyNotification() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotificationPref", MODE_PRIVATE);
        boolean isNotificationEnabled = sharedPreferences.getBoolean("notifications_enabled", false);
        // Áp dụng trạng thái thông báo (nếu cần thiết)
        // Ví dụ: kích hoạt hoặc hủy kích hoạt các dịch vụ liên quan đến thông báo
        Log.d("BaseActivity", "Notification Enabled: " + isNotificationEnabled);
    }
}
