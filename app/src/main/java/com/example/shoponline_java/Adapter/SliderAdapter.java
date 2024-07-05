package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.shoponline_java.Model.SliderItems;
import com.example.shoponline_java.R;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.viewholder> {
    private ArrayList<SliderItems> sliderItems;
    Context context;
    ViewPager2 viewPager2;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(ArrayList<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new viewholder(LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.viewholder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if (position == sliderItems.size() -2 ){
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }
        void setImage(SliderItems sliderItems){
            Glide.with(context)
                    .load(sliderItems.getUrl())
                    .into(imageView);
        }
    }
}
