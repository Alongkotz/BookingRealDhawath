package com.example.asus.bookingreal;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.asus.bookingreal.Adapter.FavoriteAdapter;
import com.example.asus.bookingreal.Database.ModelDB.Favorite;
import com.example.asus.bookingreal.Util.Common;
import com.example.asus.bookingreal.Util.RecycleItemTouchHelper;
import com.example.asus.bookingreal.Util.RecycleItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListActivity extends AppCompatActivity implements RecycleItemTouchHelperListener{

    RecyclerView recycler_fav;
    RelativeLayout rootLayout;
    CompositeDisposable compositeDisposable;

    FavoriteAdapter favoriteAdapter;
    List<Favorite> localFavorites = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        compositeDisposable = new CompositeDisposable();

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        recycler_fav = (RecyclerView) findViewById(R.id.recycle_fav);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
        recycler_fav.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_fav);


        loadFavoriteItem();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void loadFavoriteItem() {
        compositeDisposable.add(Common.favoriteRepository
                .getFavItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displayFavorite(favorites);
                    }
                }));
    }

    private void displayFavorite(List<Favorite> favorites) {
        localFavorites = favorites;
        favoriteAdapter = new FavoriteAdapter(this, favorites);
        recycler_fav.setAdapter(favoriteAdapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof  FavoriteAdapter.FavoriteViewHolder){
            String name = localFavorites.get(viewHolder.getAdapterPosition()).name;

            final Favorite deletedItem = localFavorites.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            favoriteAdapter.removeItem(deletedIndex);

            Common.favoriteRepository.delete(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout,new StringBuilder(name).append("เอาออกจาก Favorites เรียบร้อย").toString(),Snackbar.LENGTH_SHORT);
            snackbar.setAction("ยกเลิก", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.favoriteRepository.InsertFav(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();


        }
    }
}
