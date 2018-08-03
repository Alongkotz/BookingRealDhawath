package com.example.asus.bookingreal.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.bookingreal.Interface.IitemClickLister;
import com.example.asus.bookingreal.R;

public class RoomviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img_room;
    TextView txt_room;

    IitemClickLister iitemClickLister;

    ImageButton button_add,button_add2;

    public void setIitemClickLister(IitemClickLister iitemClickLister) {
        this.iitemClickLister = iitemClickLister;
    }

    public RoomviewHolder(View itemView) {
        super(itemView);

        img_room = (ImageView) itemView.findViewById(R.id.imagec);
        txt_room = (TextView) itemView.findViewById(R.id.roomname);
        button_add = (ImageButton) itemView.findViewById(R.id.add_btn1);
        button_add2 = (ImageButton)itemView.findViewById(R.id.add_btn2);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iitemClickLister.onClick(v);
    }
}
