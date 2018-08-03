package com.example.asus.bookingreal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.asus.bookingreal.Adapter.CatagoryAdapter;
import com.example.asus.bookingreal.Adapter.OrderAdapter;
import com.example.asus.bookingreal.Database.Datasouce.CartRepository;
import com.example.asus.bookingreal.Database.Datasouce.FavoriteRepository;
import com.example.asus.bookingreal.Database.Local.AONRoomDatabase;
import com.example.asus.bookingreal.Database.Local.CartDataSouce;
import com.example.asus.bookingreal.Database.Local.FavoriteDataSource;
import com.example.asus.bookingreal.Database.ModelDB.Favorite;
import com.example.asus.bookingreal.Model.Banner;
import com.example.asus.bookingreal.Model.Category;
import com.example.asus.bookingreal.Model.CheckUserResponse;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Model.Room;
import com.example.asus.bookingreal.Model.User;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;
import com.example.asus.bookingreal.Util.ProgessRequestBody;
import com.example.asus.bookingreal.Util.UploadCallback;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.asus.bookingreal.Util.Common.*;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UploadCallback {
    private static final int PICK_FILE_REQUEST = 1222;
    TextView textCname,
            textBname,
            textphone;
    SliderLayout sliderLayout;

    IBookingAPI mService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RecyclerView lstmenu;
    RecyclerView showorder;
    NotificationBadge badge;
    ImageView cart_icon;

    CircleImageView img_avat;
    Uri selectFileUri;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        mService = Common.getAPI();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);

        lstmenu = (RecyclerView) findViewById(R.id.menu1);
        lstmenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstmenu.setHasFixedSize(true);

        showorder = (RecyclerView) findViewById(R.id.showorder);
        showorder.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        showorder.setHasFixedSize(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);

        textCname = (TextView) hView.findViewById(R.id.textCname);
        textphone = (TextView) hView.findViewById(R.id.textphone);
        textBname = (TextView) hView.findViewById(R.id.textBname);
        img_avat = (CircleImageView) hView.findViewById(R.id.imageView);

        img_avat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.cUser != null)
                    chooseImage();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getBannerImage();
                getMenu();
                getAdditionList();getTimeList();
                loadOrder("2");

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getMenu();
                getAdditionList();
                getTimeList();
                loadOrder("2");
            }
        });


        initDB();
        checkSessionLogin();
    }

    private void loadOrder(String statusCode) {
        if (Common.cUser != null) {
            compositeDisposable.add(mService.getOrderByStatus(statusCode)
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
            Toast.makeText(this, "กรุณาเข้าสู่ระบบ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }
    }

    private void displayOrder(List<Order> orders) {
        OrderAdapter adapter = new OrderAdapter(this, orders);
        showorder.setAdapter(adapter);
    }

    private void checkSessionLogin() {

        if (AccountKit.getCurrentAccessToken() != null) {
            swipeRefreshLayout.setRefreshing(true);
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mService.checkUserExists(account.getPhoneNumber().toString()).enqueue(new Callback<CheckUserResponse>() {
                        @Override
                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                            CheckUserResponse userResponse = response.body();
                            if (userResponse.isExists()) {
                                mService.getUserInformation(account.getPhoneNumber().toString()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        Common.cUser = response.body();
                                        if (Common.cUser != null) {
                                            swipeRefreshLayout.setRefreshing(false);

                                            textCname.setText(cUser.getCname());
                                            textBname.setText(cUser.getBname());
                                            textphone.setText(cUser.getPhone());


                                            if (!TextUtils.isEmpty(cUser.getAvatarUrl())) {
                                                Picasso.with(getBaseContext())
                                                        .load(new StringBuilder(Common.BASE_URL)
                                                                .append("user_avatar/")
                                                                .append(Common.cUser.getAvatarUrl()).toString()).into(img_avat);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        swipeRefreshLayout.setRefreshing(false);
                                        Log.d("ERROR", t.getMessage());
                                    }
                                });
                            } else {
                                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                                finish();
                            }


                        }

                        @Override
                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                            Log.d("ERROR", t.getMessage());
                        }
                    });
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("ERROR", accountKitError.getErrorType().getMessage());

                }
            });
        } else {
            AccountKit.logOut();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void chooseImage() {
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    selectFileUri = data.getData();
                    if (selectFileUri != null && !selectFileUri.getPath().isEmpty()) {
                        img_avat.setImageURI(selectFileUri);
                        uploadFile();
                    } else
                        Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile() {
        if (selectFileUri != null) {
            File file = FileUtils.getFile(this, selectFileUri);

            String fileName = new StringBuilder(Common.cUser.getPhone()).append(FileUtils.getExtension(file.toString())).toString();

            ProgessRequestBody requestFile = new ProgessRequestBody(file, this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", fileName, requestFile);

            final MultipartBody.Part userPhone = MultipartBody.Part.createFormData("phone", Common.cUser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(userPhone, body).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(HomeActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).start();

        }
    }

    private void initDB() {
        Common.aonRoomDatabase = AONRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository
                .getInstance(CartDataSouce
                        .getInstance(Common.aonRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.aonRoomDatabase.favoriteDAO()));

    }

    private void getAdditionList() {
        compositeDisposable.add(mService.getRoom(Common.ADDITIONAL_MENU_ID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Room>>() {
                    @Override
                    public void accept(List<Room> rooms) throws Exception {
                        Common.additional = rooms;
                    }
                }));
    }

    private void getTimeList() {
        compositeDisposable.add(mService.getRoom(Common.TIME_MENU_ID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Room>>() {
                    @Override
                    public void accept(List<Room> rooms) throws Exception {
                        Common.timee = rooms;
                    }
                }));
    }

    private void getBannerImage() {
        compositeDisposable.add(mService.getBanner()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImage(banners);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (Banner item : banners)
            hashMap.put(item.getName(), item.getLink());

        for (String name : hashMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name).image(hashMap.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);

            sliderLayout.addSlider(textSliderView);
        }

    }

    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackButtonClicked) {
                super.onBackPressed();
                return;
            }
            this.isBackButtonClicked = true;
            Toast.makeText(this, "กรุณากดอีกครั้งเพื่อออก", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge) view.findViewById(R.id.badge);
        cart_icon = (ImageView) view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    private void updateCartCount() {
        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItem() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItem()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_menu) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sign_outt) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ออกจากแอพลิเคชั่น");
            builder.setMessage("คุณต้องการที่จะออกจากแอพลิเคชั่น ?");

            builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AccountKit.logOut();

                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
            });

            builder.setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } else if (id == R.id.nav_favorite) {
            if (Common.cUser != null) {
                startActivity(new Intent(HomeActivity.this, FavoriteListActivity.class));
            } else {
                Toast.makeText(this, "กรุณาล็อคอิน", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_order) {
            if (Common.cUser != null) {
                startActivity(new Intent(HomeActivity.this, ShowOrderActivity.class));
            } else {
                Toast.makeText(this, "กรุณาล็อคอิน", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_sign_in) {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        } else if (id == R.id.nav_suggection) {
            if (Common.cUser != null) {
                startActivity(new Intent(HomeActivity.this, Onboarding.class));
            } else {
                Toast.makeText(this, "กรุณาล็อคอิน", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getMenu() {
        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        displayMenu(categories);
                    }
                }));

    }

    private void displayMenu(List<Category> categories) {
        CatagoryAdapter adapter = new CatagoryAdapter(this, categories);
        lstmenu.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
        loadOrder("2");
        isBackButtonClicked = false;
    }

    @Override
    public void onProgessUpdate(int pertantage) {

    }


}
