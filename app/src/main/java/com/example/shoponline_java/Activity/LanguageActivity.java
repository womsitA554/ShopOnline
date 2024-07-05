package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoponline_java.R;

import java.util.Locale;

public class LanguageActivity extends BaseActivity {
    ImageView btnBack;
    LinearLayout btnEnglish, btnVietnamese;
    ImageView iconEnglishDone, iconVietnameseDone;
    SharedPreferences sharedPreferencesLanguage;
    SharedPreferences.Editor editorLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLocale();

        setContentView(R.layout.activity_language);


        btnBack = findViewById(R.id.btnBack);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnVietnamese = findViewById(R.id.btnVietnamese);
        iconEnglishDone = findViewById(R.id.iconEnglishDone);
        iconVietnameseDone = findViewById(R.id.iconVietnameseDone);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, SettingActivity.class));
                finish();
            }
        });

        iconEnglishDone.setVisibility(View.GONE);
        iconVietnameseDone.setVisibility(View.GONE);

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("en");
            }
        });

        btnVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("vi");
            }
        });

        sharedPreferencesLanguage = getSharedPreferences("Settings", MODE_PRIVATE);
        editorLanguage = sharedPreferencesLanguage.edit();
        String lang = sharedPreferencesLanguage.getString("My_Lang", "en");
        if (lang.equals("en")){
            iconEnglishDone.setVisibility(View.VISIBLE);
            iconVietnameseDone.setVisibility(View.GONE);
        } else if (lang.equals("vi")) {
            iconEnglishDone.setVisibility(View.GONE);
            iconVietnameseDone.setVisibility(View.VISIBLE);
        }

    }

    private void changeLanguage(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        // Update configuration based on API level
        getApplicationContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());

        editorLanguage = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editorLanguage.putString("My_Lang", langCode);
        editorLanguage.apply();

        Log.d("checkLang", "changeLanguage: " + langCode);

        recreate();
    }

    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "en");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        // Update configuration based on API level
        getApplicationContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());
    }
}