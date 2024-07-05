package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline_java.Model.Address;
import com.example.shoponline_java.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.viewholder> {
    ArrayList<Address> items;
    Context context;
    int selectedItem = RecyclerView.NO_POSITION;

    public AddressAdapter(ArrayList<Address> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.address_items, parent, false);
        return new AddressAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.viewholder holder, int position) {
        holder.tvAddress.setText(items.get(position).getAddress());

        if (selectedItem == position){
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_address_bg));
        } else {
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.address_bg));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedItem);
                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(selectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            tvAddress = itemView.findViewById(R.id.tvAddress);

        }
    }

    public Address getSelectedAddress() {
        return selectedItem != RecyclerView.NO_POSITION ? items.get(selectedItem) : null;
    }

}
