package com.example.asus.bookingreal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.asus.bookingreal.Adapter.CartAdapter;
import com.example.asus.bookingreal.Adapter.FavoriteAdapter;
import com.example.asus.bookingreal.Adapter.RoomAdapter;
import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.Database.ModelDB.Favorite;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;
import com.example.asus.bookingreal.Util.RecycleItemTouchHelper;
import com.example.asus.bookingreal.Util.RecycleItemTouchHelperListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecycleItemTouchHelperListener {

    RecyclerView recycler_cart;
    Button btn_place_order, btn_exits;
    List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter;
    CompositeDisposable compositeDisposable;
    RelativeLayout rootLayout;
    IBookingAPI mService;

    EditText edt_comment, edt_other_phone, edt_comment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();

        mService = Common.getAPI();

        recycler_cart = (RecyclerView) findViewById(R.id.recycle_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
        btn_exits = (Button) findViewById(R.id.exits);
        btn_exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        loadCartItem();

    }

    private void placeOrder() {

        if (Common.cUser != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ยืนยันรายการ");
            View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

            edt_comment = (EditText) submit_order_layout.findViewById(R.id.edt_comment1);
            edt_comment2 = (EditText) submit_order_layout.findViewById(R.id.edt_comment3);
            edt_other_phone = (EditText) submit_order_layout.findViewById(R.id.edt_comment2);

            final RadioButton rdi_user_phone = (RadioButton) submit_order_layout.findViewById(R.id.rdi_user_phone);
            final RadioButton rdi_other_phone = (RadioButton) submit_order_layout.findViewById(R.id.rdi_other_phone);

            rdi_user_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        edt_other_phone.setEnabled(false);
                }
            });
            rdi_other_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        edt_other_phone.setEnabled(true);
                }
            });

            builder.setView(submit_order_layout);

            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String orderComment = edt_comment.getText().toString();
                    final String orderSub = edt_comment2.getText().toString();
                    final String otherPhone;
                    if (rdi_user_phone.isChecked())
                        otherPhone = Common.cUser.getPhone();
                    else if (rdi_other_phone.isChecked())
                        otherPhone = edt_other_phone.getText().toString();
                    else
                        otherPhone = " ";

                    compositeDisposable.add(Common.cartRepository.getCartItem().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Cart>>() {
                                @Override
                                public void accept(List<Cart> carts) throws Exception {
                                    if (!TextUtils.isEmpty(otherPhone))
                                        sendOrderToServer(otherPhone, carts, orderComment, orderSub);
                                    else
                                        Toast.makeText(CartActivity.this, "Order Phone cant null ", Toast.LENGTH_SHORT).show();
                                }
                            })
                    );
                }
            });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("ยังไม่ได้เข้าสู่ระบบ ?");
            builder.setMessage("กรุณาเข้าสู่ระบบ หรือ สมัครสมาชิก เพื่อที่จะยืนยัน");
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(CartActivity.this, MainActivity.class));
                    finish();
                }
            }).show();

        }

    }

    private void sendOrderToServer(String otherPhone, List<Cart> carts, String orderComment, String orderSub) {
        if (carts.size() > 0) {
            String orderDetail = new Gson().toJson(carts);

            mService.summitOrder(otherPhone, orderDetail, orderComment,orderSub).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(CartActivity.this, "จองห้องเรียบร้อย", Toast.LENGTH_SHORT).show();
                    Common.cartRepository.emtryCart();
                    finish();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("ERROR", t.getMessage());
                }
            });

        }
    }

    private void loadCartItem() {
        compositeDisposable.add(Common.cartRepository
                .getCartItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartItem(carts);
                    }
                })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
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


    @Override
    protected void onResume() {
        super.onResume();
        loadCartItem();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeItem(deletedIndex);

            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append("เอาออกจาก Favorites เรียบร้อย").toString(), Snackbar.LENGTH_SHORT);
            snackbar.setAction("ยกเลิก", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deletedItem, deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();


        }
    }
}
