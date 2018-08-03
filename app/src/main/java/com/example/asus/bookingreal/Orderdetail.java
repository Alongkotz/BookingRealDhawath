package com.example.asus.bookingreal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bookingreal.Adapter.OrderDetailAdapter;
import com.example.asus.bookingreal.Adapter.RoomAdapter;
import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Orderdetail extends AppCompatActivity {

    TextView txt_order_id, txt_order_phone, txt_order_comment, txt_order_status, txt_order_comment2;
    Button cancel_btn;
    RecyclerView recycler_order_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        txt_order_id = (TextView) findViewById(R.id.txt_order_id);
        txt_order_phone = (TextView) findViewById(R.id.txt_order_phone);
        txt_order_comment = (TextView) findViewById(R.id.txt_order_comment);
        txt_order_comment2 = (TextView) findViewById(R.id.txt_subject);
        txt_order_status = (TextView) findViewById(R.id.txt_order_status);

        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrder();
            }
        });

        recycler_order_detail = (RecyclerView) findViewById(R.id.recycle_order_detail);
        recycler_order_detail.setLayoutManager(new LinearLayoutManager(this));
        recycler_order_detail.setHasFixedSize(true);
        txt_order_id.setText(new StringBuilder("#").append(Common.currentOrder.getOrderId()));
        txt_order_phone.setText(new StringBuilder("ถูกจองโดย" + " ").append(Common.currentOrder.getUserPhone()));
        txt_order_comment.setText(new StringBuilder("จองโดยแผนก : ").append(Common.currentOrder.getOrderComment()));
        txt_order_comment2.setText(new StringBuilder("หัวข้อประชุม : ").append(Common.currentOrder.getOrderSubject()));
        txt_order_status.setText(new StringBuilder("สถานะการจอง : ").append(Common.convertCodeToStatus(Common.currentOrder.getOrderStatus())));


        displayOrderDetail();

    }

    private void CancelOrder() {
        IBookingAPI bookingAPI = Common.getAPI();
        bookingAPI.CancelOrder(String.valueOf(Common.currentOrder.getOrderId()), Common.cUser.getPhone()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(Orderdetail.this, response.body(), Toast.LENGTH_SHORT).show();
                if (response.body().contains("ยกเลิกเรียบร้อย"))
                    finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("DEBUG", t.getMessage());
            }
        });
    }


    private void displayOrderDetail() {
        List<Cart> orderDetail = new Gson().fromJson(Common.currentOrder.getOrderDetail(),
                new TypeToken<List<Cart>>() {
                }.getType());
        recycler_order_detail.setAdapter(new OrderDetailAdapter(this, orderDetail));

    }
}
