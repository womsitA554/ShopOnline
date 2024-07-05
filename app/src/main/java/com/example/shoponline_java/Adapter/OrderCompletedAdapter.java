package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shoponline_java.Activity.OrderActivity;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.Model.Order;
import com.example.shoponline_java.R;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderCompletedAdapter extends RecyclerView.Adapter<OrderCompletedAdapter.viewholder> {
    ArrayList<Order> items;
    Context context;

    public OrderCompletedAdapter(ArrayList<Order> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderCompletedAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_completed_items, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCompletedAdapter.viewholder holder, int position) {
        holder.tvPrice.setText("$" + items.get(position).getTotalPrice());
        holder.tvTitle.setText(items.get(position).getOrderId());
        holder.tvAddress.setText(items.get(position).getAddress());
        holder.tvTime.setText(items.get(position).getOrderTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvTime, tvAddress;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvAddress = itemView.findViewById(R.id.tvAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Order order = items.get(position);
                        Intent intent = new Intent(context, OrderActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        intent.putExtra("totalPrice", order.getTotalPrice());
                        intent.putExtra("address", order.getAddress());
                        intent.putExtra("orderTime", order.getOrderTime());
                        intent.putExtra("orderPayment", order.getPaymentMethod());
                        intent.putExtra("cartItems", (Serializable) order.getCartItems());


                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
