package com.example.shoponline_java.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shoponline_java.Activity.SearchActivity;
import com.example.shoponline_java.Adapter.PopularAdapter;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishListFragment extends Fragment {
    RecyclerView recyclerViewSearch;
    PopularAdapter adapter;
    ArrayList<Item> items;
    ArrayList<Item> allItems;
    String userId;
    EditText etSearch;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        etSearch = view.findViewById(R.id.etSearch);

        userId = getActivity().getIntent().getStringExtra("userId");

        recyclerViewSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        items = new ArrayList<>();
        allItems = new ArrayList<>();
        adapter = new PopularAdapter(getActivity(), items, userId);
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
        return view;
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