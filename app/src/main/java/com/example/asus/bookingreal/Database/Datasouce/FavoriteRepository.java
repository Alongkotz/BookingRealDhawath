package com.example.asus.bookingreal.Database.Datasouce;

import com.example.asus.bookingreal.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements IFavoriteDataSource {

    private IFavoriteDataSource favoriteDataSource;

    public FavoriteRepository(IFavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    private static FavoriteRepository instance;
    public static FavoriteRepository getInstance(IFavoriteDataSource favoriteDataSource){
        if(instance == null)
            instance = new FavoriteRepository(favoriteDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItem() {
        return favoriteDataSource.getFavItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void InsertFav(Favorite... favorites) {
        favoriteDataSource.InsertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
    favoriteDataSource.delete(favorite);
    }
}
