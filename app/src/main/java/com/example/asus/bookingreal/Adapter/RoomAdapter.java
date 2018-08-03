package com.example.asus.bookingreal.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.asus.bookingreal.CartActivity;
import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.Database.ModelDB.Favorite;
import com.example.asus.bookingreal.Interface.IitemClickLister;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Model.Room;
import com.example.asus.bookingreal.R;
import com.example.asus.bookingreal.Util.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RoomAdapter extends RecyclerView.Adapter<RoomviewHolder> {

    Context context;
    List<Room> roomList;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }


    @NonNull
    @Override
    public RoomviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_item_layout, null);
        return new RoomviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RoomviewHolder holder, final int position) {
        holder.txt_room.setText(roomList.get(position).Name);
        holder.button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddtoCart(position);
            }
        });
        Picasso.with(context).load(roomList.get(position).Link).into(holder.img_room);
        holder.setIitemClickLister(new IitemClickLister() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked ", Toast.LENGTH_SHORT).show();
            }
        });

        if (Common.favoriteRepository.isFavorite(Integer.parseInt(roomList.get(position).ID)) == 1)
            holder.button_add2.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            holder.button_add2.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        holder.button_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.favoriteRepository.isFavorite(Integer.parseInt(roomList.get(position).ID)) != 1) {
                    addOrRemoveFavorite(roomList.get(position), true);
                    holder.button_add2.setImageResource(R.drawable.ic_favorite_black_24dp);
                } else {
                    addOrRemoveFavorite(roomList.get(position), false);
                    holder.button_add2.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

            }
        });
    }

    private void addOrRemoveFavorite(Room room, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.id = room.ID;
        favorite.name = room.Name;
        favorite.link = room.Link;
        favorite.menuId = room.MenuId;

        if (isAdd)
            Common.favoriteRepository.InsertFav(favorite);
        else
            Common.favoriteRepository.delete(favorite);
    }

    private void showAddtoCart(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_to_choose, null);
        ImageView img_product = (ImageView) view.findViewById(R.id.cart);
        final ElegantNumberButton txt_count = (ElegantNumberButton) view.findViewById(R.id.text_count);
        final TextView text_room = (TextView) view.findViewById(R.id.text_cart);
        RadioButton rdt_customer = (RadioButton)view.findViewById(R.id.rdt_customer);
        RadioButton rdt_staff = (RadioButton)view.findViewById(R.id.rdt_staff);

        rdt_customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.CustStaf = "ลูกค้า";
            }
        });
        rdt_staff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.CustStaf = "พนักงาน";
            }
        });
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date1 = dayOfMonth + " /" + (month + 1) + " /" + year;
                Common.date = date1;
            }
        });

        RecyclerView recycler_additional = (RecyclerView) view.findViewById(R.id.recycle_addi);
        recycler_additional.setLayoutManager(new LinearLayoutManager(context));
        recycler_additional.setHasFixedSize(true);

        MultichoiceAdp adp = new MultichoiceAdp(context, Common.additional);
        recycler_additional.setAdapter(adp);


        RecyclerView recycler_time = (RecyclerView) view.findViewById(R.id.recycle_time);
        recycler_time.setLayoutManager(new LinearLayoutManager(context));
        recycler_time.setHasFixedSize(true);

        final MultichoiceAdpT adpt = new MultichoiceAdpT(context, Common.timee);
        recycler_time.setAdapter(adpt);


        Picasso.with(context).load(roomList.get(position).Link).into(img_product);
        text_room.setText(roomList.get(position).Name);
        builder.setView(view);
        builder.setNegativeButton("ยืนยัน ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.countT = Integer.toString(adpt.getCountcheck());
                if(Common.countT.equals("0")){
                    Toast.makeText(context, "กรุณาเลือกเวลาที่ต้องการ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.date == null) {
                    Toast.makeText(context,Common.countT, Toast.LENGTH_SHORT).show();
                    return;
                }

                showConfirmdialog(position, txt_count.getNumber());
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void showConfirmdialog(final int position, final String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_add, null);
        ImageView img_product = (ImageView) view.findViewById(R.id.img_product);
        final TextView text_room = (TextView) view.findViewById(R.id.text_cart);
        final TextView text_cust = (TextView) view.findViewById(R.id.text_CustStaff);
        final TextView text_time = (TextView) view.findViewById(R.id.text_time);
        final TextView text_date = (TextView) view.findViewById(R.id.text_date);
        final TextView text_addition = (TextView) view.findViewById(R.id.text_additional);

        Picasso.with(context).load(roomList.get(position).Link).into(img_product);
        text_room.setText(new StringBuilder(roomList.get(position).Name).append(" " + number) + " ท่าน");
        text_date.setText(new StringBuilder("วันที่ : ").append(Common.date).toString());
        text_cust.setText(new StringBuilder("ประชุมกับ : ").append(Common.CustStaf).toString());
        StringBuilder add_final_comment = new StringBuilder("");
        for (String line : Common.additionaladd)
            add_final_comment.append(line).append(", ");
        text_addition.setText(add_final_comment);

        StringBuilder add_final_time = new StringBuilder("");
        for (String line : Common.timeeadd)
            add_final_time.append(line).append(", ").append("\n");
        text_time.setText(new StringBuilder("เวลา : ").append("\n").append(add_final_time).append(" รวม ").append(Common.countT).append(" ชม. "));


        builder.setNegativeButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    Cart cartItem = new Cart();
                    cartItem.name = text_room.getText().toString();
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.customer  = Common.CustStaf.toString();
                    cartItem.time = text_time.getText().toString();
                    cartItem.date = Common.date;
                    cartItem.additionals = text_addition.getText().toString();
                    cartItem.link = roomList.get(position).Link;
                    Common.cartRepository.insertToCart(cartItem);
                    Log.d("Alongkot_DEBUG", new Gson().toJson(cartItem));
                    Toast.makeText(context, "เรียบร้อย", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context, "ผิดพลาด", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setView(view);
        builder.show();

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
