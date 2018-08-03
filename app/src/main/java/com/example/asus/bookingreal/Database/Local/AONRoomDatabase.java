package com.example.asus.bookingreal.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.Database.ModelDB.Favorite;

@Database(entities = {Cart.class, Favorite.class}, version = 1)
public abstract class AONRoomDatabase extends RoomDatabase {


    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    private static AONRoomDatabase instance;

    public static AONRoomDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, AONRoomDatabase.class, "AlongKot_BookingDB")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
