package com.example.shoponline_java.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shoponline_java.Adapter.SizeAdapter;
import com.example.shoponline_java.Adapter.SliderAdapter;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.Model.SliderItems;
import com.example.shoponline_java.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ImageView btnBack;
    ViewPager2 viewPage2;
    Item item;
    int numberOrder = 1;
    RecyclerView recyclerViewSize;
    AppCompatButton btnAddCart;
    TextView tvTitle, tvPrice, tvRating, tvContent;
    RatingBar ratingBar;
    FirebaseHelper firebaseHelper;
    SizeAdapter sizeAdapter;

    String userId;
    int selectedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        firebaseHelper = new FirebaseHelper();
        recyclerViewSize = findViewById(R.id.recyclerViewSize);
        btnAddCart = findViewById(R.id.btnAddCart);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvRating = findViewById(R.id.tvRating);
        tvContent = findViewById(R.id.tvContent);
        ratingBar = findViewById(R.id.ratingBar);
        viewPage2 = findViewById(R.id.viewPage2);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getBundles();
        setUpBanner();
        setUpRecycleSize();
    }


    private void setUpRecycleSize() {
        ArrayList<String> sizeItems = new ArrayList<>();
        sizeItems.add(new String("XS"));
        sizeItems.add(new String("S"));
        sizeItems.add(new String("M"));
        sizeItems.add(new String("L"));
        sizeItems.add(new String("Xl"));
        sizeItems.add(new String("XXL"));

        recyclerViewSize.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        sizeAdapter = new SizeAdapter(sizeItems);
        recyclerViewSize.setAdapter(sizeAdapter);
    }

    private void setUpBanner() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < item.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(item.getPicUrl().get(i)));
        }

        viewPage2.setAdapter(new SliderAdapter(sliderItems, viewPage2));
        viewPage2.setClipToPadding(false);
        viewPage2.setClipChildren(false);
        viewPage2.setOffscreenPageLimit(3);
        viewPage2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        userId = getIntent().getStringExtra("userId");
        Log.d("check2", userId);
        item = (Item) getIntent().getSerializableExtra("object");
        tvTitle.setText(item.getTitle());
        tvPrice.setText("$" + item.getPrice());
        ratingBar.setRating((float) item.getRating());
        tvRating.setText("(" + item.getRating() + ")");
        tvContent.setText(item.getDescription());

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSize = ((SizeAdapter) recyclerViewSize.getAdapter()).getSelectedSize();
                if (selectedSize != null) {
                    if (selectedSize != null) {
                        // Thêm sản phẩm vào giỏ hàng với kích cỡ đã chọn
                        item.addSize(selectedSize, 1);
                        item.setNumberInCart(item.getNumberInCart() + 1);
                        firebaseHelper.addCartItem(userId, item.getTitle(), item);
                        Toast.makeText(DetailActivity.this, "The product has been added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Please select size before adding to cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Please log in to add products to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}