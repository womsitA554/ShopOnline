package com.example.shoponline_java.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline_java.Adapter.PopularAdapter;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ImageView btnBack;
    RecyclerView recyclerViewSearch;
    PopularAdapter adapter;
    ArrayList<Item> items;
    ArrayList<Item> allItems;
    String userId;
    EditText etSearch;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnBack = findViewById(R.id.btnBack);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        etSearch = findViewById(R.id.etSearch);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userId = getIntent().getStringExtra("userId");

        recyclerViewSearch.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        items = new ArrayList<>();
        allItems = new ArrayList<>();
        adapter = new PopularAdapter(SearchActivity.this, items, userId);
        recyclerViewSearch.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        loadTitleToLowerCase();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadTitleToLowerCase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allItems.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    allItems.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchActivity", "Error: " + error.getMessage());
            }
        });
    }

    private void search(String query) {
        items.clear();
        for (Item item : allItems) {
            if (item.getTitle().toLowerCase().contains(query)) {
                items.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}