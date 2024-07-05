package com.example.shoponline_java.Adapter;

import android.content.Context;
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
import com.example.shoponline_java.Helper.ChangeNumberItemsListener;
import com.example.shoponline_java.Helper.FirebaseHelper;
import com.example.shoponline_java.Model.Item;
import com.example.shoponline_java.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Item> itemsSelected;
    ChangeNumberItemsListener changeNumberItemsListener;
    Context context;
    DatabaseReference databaseReference;
    FirebaseHelper firebaseHelper;
    String userId;
    OnCartEmptyListener onCartEmptyListener;

    public interface OnCartEmptyListener {
        void onCartEmpty();
    }

    public CartAdapter(ArrayList<Item> itemsSelected, Context context, String userId, ChangeNumberItemsListener changeNumberItemsListener, OnCartEmptyListener onCartEmptyListener) {
        this.itemsSelected = itemsSelected;
        this.context = context;
        this.userId = userId;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.onCartEmptyListener = onCartEmptyListener;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("sizes");
        firebaseHelper = new FirebaseHelper();
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        Item currentItem = itemsSelected.get(position);
        holder.tvTitle.setText(currentItem.getTitle());
        holder.tvFeeEachItem.setText("$" + currentItem.getPrice());
        holder.tvTotalPriceEachItem.setText("$" + Math.round(currentItem.getNumberInCart() * currentItem.getPrice()));
        holder.tvQuantity.setText(String.valueOf(currentItem.getNumberInCart()));
        Glide.with(context)
                .load(currentItem.getPicUrl().get(0))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.pic);

        // Retrieve size data from Firebase
        HashMap<String, Integer> sizes = currentItem.getSizes();
        if (sizes != null) {
            StringBuilder sizeText = new StringBuilder();
            for (String size : sizes.keySet()) {
                sizeText.append(size);
            }
            holder.tvSize.setText("Size: " + sizeText.toString().trim());
        }

        holder.btnPlus.setOnClickListener(v -> {
            if (userId != null) {
                Log.d("check3", userId);
            } else {
                Log.d("check3", "null");
            }
            currentItem.setNumberInCart(currentItem.getNumberInCart() + 1);
            firebaseHelper.updateCartItemQuantity(userId, currentItem.getTitle(), currentItem.getNumberInCart());
            changeNumberItemsListener.changed();
            notifyDataSetChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (currentItem.getNumberInCart() <= 1) {
                itemsSelected.remove(currentItem);
                firebaseHelper.removeCartItem(userId, currentItem.getTitle());
                if (itemsSelected.isEmpty()) {
                    onCartEmptyListener.onCartEmpty();
                }
            } else {
                currentItem.setNumberInCart(currentItem.getNumberInCart() - 1);
                firebaseHelper.updateCartItemQuantity(userId, currentItem.getTitle(), currentItem.getNumberInCart());
                changeNumberItemsListener.changed();
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return itemsSelected.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvFeeEachItem, tvTotalPriceEachItem, btnMinus, tvQuantity, btnPlus, tvSize;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tvSize = itemView.findViewById(R.id.tvSize);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFeeEachItem = itemView.findViewById(R.id.tvFeeEachItem);
            tvTotalPriceEachItem = itemView.findViewById(R.id.tvTotalPriceEachItem);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            pic = itemView.findViewById(R.id.pic);
        }
    }

    public ArrayList<Item> getAllItemInCart(){
        return itemsSelected;
    }
}

