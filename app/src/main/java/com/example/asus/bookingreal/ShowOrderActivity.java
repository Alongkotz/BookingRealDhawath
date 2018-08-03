package com.example.asus.bookingreal;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asus.bookingreal.Adapter.OrderAdapter;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowOrderActivity extends AppCompatActivity {

    IBookingAPI mService;
    RecyclerView recycler_orders;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        mService = Common.getAPI();

        recycler_orders = (RecyclerView) findViewById(R.id.recycle_order);
        recycler_orders.setLayoutManager(new LinearLayoutManager(this));
        recycler_orders.setHasFixedSize(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.button_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.order_new) {
                    loadOrder("0");
                } else if (item.getItemId() == R.id.order_cancle) {
                    loadOrder("-1");
                } else if (item.getItemId() == R.id.order_processed) {
                    loadOrder("2");
                } else if (item.getItemId() == R.id.order_processing) {
                    loadOrder("1");
                }
                return true;
            }
        });

    }

    private void loadOrder(String statusCode) {
        if (Common.cUser != null) {
            compositeDisposable.add(mService.getOrderByStatus( statusCode)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Order>>() {
                        @Override
                        public void accept(List<Order> orders) throws Exception {
                            displayOrder(orders);
                        }
                    })
            );
        } else {
            Toast.makeText(this,     "กรุณาเข้าสู่ระบบ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayOrder(List<Order> orders) {
        OrderAdapter adapter = new OrderAdapter(this, orders);
        recycler_orders.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
