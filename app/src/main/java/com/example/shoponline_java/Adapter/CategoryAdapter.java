package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoponline_java.Model.Category;
import com.example.shoponline_java.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> categoryItems;
    Context context;

    public CategoryAdapter(ArrayList<Category> categoryItems) {
        this.categoryItems = categoryItems;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cateory_items, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
        holder.tvTitle.setText(categoryItems.get(position).getTitle());
        Glide.with(context).load(categoryItems.get(position).getPicUrl()).into(holder.picUrl);
    }

    @Override
    public int getItemCount() {
        return categoryItems.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView picUrl;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            picUrl = itemView.findViewById(R.id.picUrl);
        }
    }
}
