package com.example.asus.bookingreal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.bookingreal.Interface.IitemClickLister;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Model.User;
import com.example.asus.bookingreal.Orderdetail;
import com.example.asus.bookingreal.R;
import com.example.asus.bookingreal.Util.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    Context context;
    List<Order> orderList;


    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        holder.txt_order_id.setText(new StringBuilder("#").append(orderList.get(position).getOrderId()));
        holder.txt_order_phone.setText(new StringBuilder("ถูกจองโดย" + " ").append(orderList.get(position).getUserPhone()));
        holder.txt_order_comment.setText(new StringBuilder("จองโดยแผนก : ").append(orderList.get(position).getOrderComment()));
        holder.txt_order_subject.setText(new StringBuilder("หัวข้อที่ประชุม : ").append(orderList.get(position).getOrderSubject()));
        holder.txt_order_status.setText(new StringBuilder("สถานะการจอง : ").append(Common.convertCodeToStatus(orderList.get(position).getOrderStatus())));

        holder.setIitemClickLister(new IitemClickLister() {
            @Override
            public void onClick(View v) {
                Common.currentOrder  = orderList.get(position);
                context.startActivity(new Intent(context, Orderdetail.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
