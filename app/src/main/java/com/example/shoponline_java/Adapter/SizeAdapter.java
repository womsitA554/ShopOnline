package com.example.shoponline_java.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoponline_java.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.viewholder> {
    ArrayList<String> sizeItems;
    Context context;
    int selectedPosition = -1;
    int lastSelectedPosition = -1;

    public SizeAdapter(ArrayList<String> sizeItems) {
        this.sizeItems = sizeItems;
    }

    @NonNull
    @Override
    public SizeAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.size_items, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.viewholder holder, int position) {
        holder.tvSize.setText(sizeItems.get(position));
        View rootView = holder.itemView;
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if (selectedPosition == holder.getAdapterPosition()){
            holder.sizeLayout.setBackgroundResource(R.drawable.selected);
            holder.tvSize.setTextColor(context.getResources().getColor(R.color.purple));
        } else {
            holder.sizeLayout.setBackgroundResource(R.drawable.unselected);
            holder.tvSize.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return sizeItems.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tvSize;
        ConstraintLayout sizeLayout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvSize = itemView.findViewById(R.id.tvSize);
            sizeLayout = itemView.findViewById(R.id.sizeLayout);

        }
    }
    public String getSelectedSize(){
        if (selectedPosition != -1 && selectedPosition < sizeItems.size()){
            return sizeItems.get(selectedPosition);
        }
        return null;
    }

}
