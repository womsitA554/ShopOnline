package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.shoponline_java.Activity.DetailActivity;
import com.example.shoponline_java.Model.Category;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.viewholder> {
    ArrayList<Item> popularItems;
    Context context;
    String userId;

    public PopularAdapter(Context context, ArrayList<Item> popularItems, String userId) {
        this.context = context;
        this.popularItems = popularItems;
        this.userId = userId;
    }

    @NonNull
    @Override
    public PopularAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.popular_items, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.tvTitle.setText(popularItems.get(position).getTitle());
        holder.tvRating.setText("("+popularItems.get(position).getRating()+")");
        holder.tvOldPrice.setText("$"+popularItems.get(position).getOldPrice());
        holder.tvNewPrice.setText("$"+popularItems.get(position).getPrice());
        holder.tvReview.setText(""+popularItems.get(position).getReview());
        holder.ratingBar.setRating((float) popularItems.get(position).getRating());
        holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        RequestOptions options = new RequestOptions();
        options = options.transform(new CenterCrop());

        Glide.with(context).load(popularItems.get(position).getPicUrl().get(0)).apply(options).into(holder.picUrlPopular);

        final int itemPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", popularItems.get(itemPosition));
                intent.putExtra("userId", userId);
                Log.d("Detail", userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularItems.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRating, tvOldPrice,tvNewPrice,tvReview;
        ImageView picUrlPopular;
        RatingBar ratingBar;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            picUrlPopular = itemView.findViewById(R.id.picUrlPopular);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvNewPrice = itemView.findViewById(R.id.tvNewPrice);
            tvReview = itemView.findViewById(R.id.tvReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}
