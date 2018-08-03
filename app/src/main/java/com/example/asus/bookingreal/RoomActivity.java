package com.example.asus.bookingreal;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bookingreal.Adapter.CartAdapter;
import com.example.asus.bookingreal.Adapter.RoomAdapter;
import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Model.Room;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RoomActivity extends AppCompatActivity{
    IBookingAPI mService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RecyclerView lstroom;
    TextView txt_banner;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mService = Common.getAPI();

        lstroom = (RecyclerView) findViewById(R.id.recycle_room);
        lstroom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lstroom.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadListDrink(Common.currentCategory.ID);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadListDrink(Common.currentCategory.ID);
            }
        });
    }

    private void loadListDrink(String id) {
        compositeDisposable.add(mService.getRoom(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Room>>() {
            @Override
            public void accept(List<Room> rooms) throws Exception {
                displayRoomList(rooms);
            }
        }));
    }

    private void displayRoomList(List<Room> rooms) {
        RoomAdapter adapter = new RoomAdapter(this, rooms);
        lstroom.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


}

