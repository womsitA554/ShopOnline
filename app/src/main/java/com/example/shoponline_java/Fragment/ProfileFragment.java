package com.example.shoponline_java.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shoponline_java.Activity.OrderCompletedActivity;
import com.example.shoponline_java.Activity.SettingActivity;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.User;
import com.example.shoponline_java.R;

public class ProfileFragment extends Fragment {
    LinearLayout btnSetting, btnYourOrder;
    FirebaseHelper firebaseHelper;
    String phoneNumber;
    TextView tvPhoneNumber;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnSetting = view.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent); // Start activity for result
            }
        });

        btnYourOrder = view.findViewById(R.id.btnYourOrder);
        btnYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderCompletedActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent); // Start activity for result
            }
        });

        userId = getActivity().getIntent().getStringExtra("userId");


        firebaseHelper = new FirebaseHelper();

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        firebaseHelper.getPhoneNumber(userId, new FirebaseHelper.UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user!=null){
                    phoneNumber = user.getPhoneNumber();
                    tvPhoneNumber.setText(phoneNumber);
                } else {
                    tvPhoneNumber.setText("Phone Number: Not found");
                }
            }
        });

        return view;
    }

}
