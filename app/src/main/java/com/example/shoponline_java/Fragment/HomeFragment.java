package com.example.shoponline_java.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.shoponline_java.Activity.SearchActivity;
import com.example.shoponline_java.Adapter.CategoryAdapter;
import com.example.shoponline_java.Adapter.PopularAdapter;
import com.example.shoponline_java.Adapter.SliderAdapter;
import com.example.shoponline_java.Model.Category;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.Model.SliderItems;
import com.example.shoponline_java.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ViewPager2 viewPager2;
    FirebaseDatabase database;
    ProgressBar progressBarBanner, progressBarOfficial, progressBarPopular;
    RecyclerView recyclerViewOfficial, recyclerViewPopular;
    String userId;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    ImageView iconSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = view.findViewById(R.id.viewPage2);
        progressBarBanner = view.findViewById(R.id.progressBarBanner);
        progressBarOfficial = view.findViewById(R.id.progressBarOfficial);
        progressBarPopular = view.findViewById(R.id.progressBarPopular);
        recyclerViewOfficial = view.findViewById(R.id.recyclerViewOfficial);
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);
        database = FirebaseDatabase.getInstance();
        setUpBanner();
        setUpCategory();
        setUpPopularItems();

        iconSearch = view.findViewById(R.id.iconSearch);

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SearchActivity.class);
                intent1.putExtra("userId", userId);
                startActivity(intent1);
            }
        });

        return view;
    }

    private void setUpPopularItems() {
        DatabaseReference myRef = database.getReference("Items");
        progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<Item> popularItems = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        popularItems.add(issue.getValue(Item.class));
                    }
                    if (!popularItems.isEmpty()){
                        Intent intent = getActivity().getIntent();
                        if (intent != null) {
                            userId = intent.getStringExtra("userId");
                            if (userId != null) {
                                Log.d("HomeFragment", "UserId: " + userId);
                            } else {
                                Log.e("HomeFragment", "UserId is null");
                            }
                        }
                        recyclerViewPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        recyclerViewPopular.setAdapter(new PopularAdapter(getContext(), popularItems, userId));
                        recyclerViewPopular.setNestedScrollingEnabled(true);
                        progressBarPopular.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpCategory() {
        DatabaseReference myRef = database.getReference("Category");
        progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<Category> categoryItems = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        categoryItems.add(issue.getValue(Category.class));
                    }
                    if (!categoryItems.isEmpty()){
                        recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerViewOfficial.setAdapter(new CategoryAdapter(categoryItems));
                        recyclerViewOfficial.setNestedScrollingEnabled(true);
                        progressBarOfficial.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banner(items);
                    progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBarBanner.setVisibility(View.GONE);
            }
        });
    }

    private void banner(ArrayList<SliderItems> items){
        viewPager2.setAdapter(new SliderAdapter(items, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000); // thời gian chuyển đổi (3 giây)
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2.getAdapter() != null){
                int itemCount = viewPager2.getAdapter().getItemCount();
                int currentItem = viewPager2.getCurrentItem();
                int nextItem = (currentItem + 1) % itemCount;
                viewPager2.setCurrentItem(nextItem, true);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }
}
