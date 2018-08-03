package com.example.asus.bookingreal.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.bookingreal.Interface.IitemClickLister;
import com.example.asus.bookingreal.R;

public class CatagoryView extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView menu;
    TextView namemenu;

    IitemClickLister iitemClickLister;

    public void setIitemClickLister(IitemClickLister iitemClickLister){
        this.iitemClickLister = iitemClickLister;
    }
    public CatagoryView(View itemView) {
        super(itemView);
        menu  = (ImageView)itemView.findViewById(R.id.imageb);
        namemenu = (TextView)itemView.findViewById(R.id.menuname);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            iitemClickLister.onClick(v);
    }
}
