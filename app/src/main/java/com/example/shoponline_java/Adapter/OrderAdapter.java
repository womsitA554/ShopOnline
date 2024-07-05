package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;

import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;

    public OrderAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        Glide.with(context)
                .load(item.getPicUrl().get(0))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.pic);
        holder.tvItemName.setText(item.getTitle());
        holder.tvItemPrice.setText("$" + (item.getPrice() * item.getNumberInCart()));
        // Assuming item.getSizes() returns a Map<String, Integer>
        Map<String, Integer> sizesMap = item.getSizes();
        if (sizesMap != null && !sizesMap.isEmpty()) {
            String sizes = TextUtils.join(", ", sizesMap.keySet());
            holder.tvSize.setText(sizes);
        } else {
            holder.tvSize.setText("N/A");
        }
        holder.tvItemQuantity.setText(String.valueOf(item.getNumberInCart()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView tvItemName, tvItemPrice, tvItemQuantity, tvSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            tvItemName = itemView.findViewById(R.id.tvTitle);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvItemPrice = itemView.findViewById(R.id.tvPrice);
            tvItemQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
