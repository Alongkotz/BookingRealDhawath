package com.example.asus.bookingreal.Util;

import android.support.v7.widget.RecyclerView;

public interface RecycleItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int position);
}
