package com.example.asus.bookingreal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.bookingreal.Interface.IitemClickLister;
import com.example.asus.bookingreal.Model.Category;
import com.example.asus.bookingreal.R;
import com.example.asus.bookingreal.RoomActivity;
import com.example.asus.bookingreal.Util.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryView> {
    Context context;
    List<Category> categories;

    public CatagoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override


    public CatagoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, null);
        return new CatagoryView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatagoryView holder, final int position) {
        Picasso.with(context).load(categories.get(position).Link).into(holder.menu);
        holder.namemenu.setText(categories.get(position).Name);
        holder.setIitemClickLister(new IitemClickLister() {
            @Override
            public void onClick(View v) {
                    Common.currentCategory = categories.get(position);
                    context.startActivity(new Intent(context, RoomActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
