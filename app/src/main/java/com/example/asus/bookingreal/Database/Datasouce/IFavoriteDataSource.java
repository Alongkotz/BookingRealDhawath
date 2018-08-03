package com.example.asus.bookingreal.Database.Datasouce;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.asus.bookingreal.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavItem();

    int isFavorite(int itemId);
    void InsertFav(Favorite...favorites);
    void delete(Favorite favorite);
}
