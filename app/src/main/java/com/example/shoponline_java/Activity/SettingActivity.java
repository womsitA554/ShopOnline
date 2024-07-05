package com.example.shoponline_java.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.shoponline_java.Fragment.ProfileFragment;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Helper.NotificationService;
import com.example.shoponline_java.R;

public class SettingActivity extends BaseActivity {

    LinearLayout btnLanguage;
    AppCompatButton btnLogout;
    SwitchCompat btnSwitchTheme, btnSwitchNotification;
    SharedPreferences sharedPreferencesTheme, sharedPreferencesNotification;
    SharedPreferences.Editor editorTheme, editorNotification;
    boolean isNightMode;
    String userId;
    ImageView btnBack;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnSwitchTheme = findViewById(R.id.btnSwitchTheme);
        btnLanguage = findViewById(R.id.btnLanguage);
        btnBack = findViewById(R.id.btnBack);
        btnSwitchNotification = findViewById(R.id.btnSwitchNotification);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LanguageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogout.setOnClickListener(v -> setBtnLogout());

        // switch theme
        sharedPreferencesTheme = getSharedPreferences("ThemePref", MODE_PRIVATE);
        editorTheme = sharedPreferencesTheme.edit();
        isNightMode = sharedPreferencesTheme.getBoolean("dark_theme", false);
        userId = getIntent().getStringExtra("userId");

        if (isNightMode) {
            btnSwitchTheme.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        btnSwitchTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTheme();
            }
        });

        sharedPreferencesNotification = getSharedPreferences("NotificationPref", MODE_PRIVATE);
        editorNotification = sharedPreferencesNotification.edit();
        boolean isNotificationEnabled = sharedPreferencesNotification.getBoolean("notifications_enabled", false);
        btnSwitchNotification.setChecked(isNotificationEnabled);


        btnSwitchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = btnSwitchNotification.isChecked();
                editorNotification.putBoolean("notifications_enabled", isChecked);
                editorNotification.apply();
                if (isChecked) {
                    requestNotificationPermission();
                } else {
                    requestNotificationPermission();
                }
            }
        });
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API level 33 for Android 13
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                showNotification();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            }
        } else {
            if (!areNotificationsEnabled()) {
                showNotificationSettingsDialog();
            } else {
                showNotificationSettingsDialog();
            }
        }
    }

    private boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            return notificationManager.areNotificationsEnabled();
        }
        return true;
    }

    private void showNotificationSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Notifications")
                .setMessage("Notifications are currently disabled. Would you like to enable them in settings?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showNotification();
            } else {
                Toast.makeText(SettingActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                editorNotification.putBoolean("notifications_enabled", false);
                editorNotification.apply();
                btnSwitchNotification.setChecked(false);
            }
        }
    }

    private void showNotification() {
        Toast.makeText(SettingActivity.this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
    }


    private void newTheme() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editorTheme.putBoolean("dark_theme", false);
            isNightMode = false;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editorTheme.putBoolean("dark_theme", true);
            isNightMode = true;
        }
        editorTheme.apply();
    }

    private void setBtnLogout() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("loginSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.clear();
        editor.apply();

        FirebaseHelper.logOut();

        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
