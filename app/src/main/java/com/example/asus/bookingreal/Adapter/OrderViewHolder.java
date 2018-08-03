package com.example.asus.bookingreal.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.asus.bookingreal.Interface.IitemClickLister;
import com.example.asus.bookingreal.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    IitemClickLister iitemClickLister;

    public void setIitemClickLister(IitemClickLister iitemClickLister) {
        this.iitemClickLister = iitemClickLister;
    }

    public TextView txt_order_id,txt_order_phone,txt_order_comment,txt_order_status,txt_order_subject;
    public OrderViewHolder(View itemView) {
        super(itemView);

        txt_order_id = (TextView)itemView.findViewById(R.id.txt_order_id);
        txt_order_phone = (TextView)itemView.findViewById(R.id.txt_order_phone);
        txt_order_comment = (TextView)itemView.findViewById(R.id.txt_order_comment);
        txt_order_subject = (TextView)itemView.findViewById(R.id.txt_subject);
        txt_order_status = (TextView)itemView.findViewById(R.id.txt_order_status);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iitemClickLister.onClick(v);

    }
}
