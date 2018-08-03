package com.example.asus.bookingreal.Util;

import com.example.asus.bookingreal.Database.Datasouce.CartRepository;
import com.example.asus.bookingreal.Database.Datasouce.FavoriteRepository;
import com.example.asus.bookingreal.Database.Local.AONRoomDatabase;
import com.example.asus.bookingreal.Database.ModelDB.Cart;
import com.example.asus.bookingreal.Model.Category;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Model.Room;
import com.example.asus.bookingreal.Model.User;
import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static final String BASE_URL = "https://4affd2c1.ngrok.io/PhpProject1/";
    public static final String ADDITIONAL_MENU_ID = "7";
    public static final String TIME_MENU_ID = "8";

    public static User cUser = null;
    public static Category currentCategory = null;
    public static Order currentOrder = null;

    public static List<Room> additional = new ArrayList<>();
    public static List<String> additionaladd = new ArrayList<>();

    public static List<Room> timee = new ArrayList<>();
    public static List<String> timeeadd = new ArrayList<>();

    public static String time = null;
    public static String date = null;
    public static String CustStaf = null;
    public static String countT = null;

    public static AONRoomDatabase aonRoomDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;

    public static IBookingAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IBookingAPI.class);
    }

    public static String convertCodeToStatus(int orderStatus) {
        switch (orderStatus){
            case 0:
                return "รายการจองใหม่";
            case 1:
                return "กำลังดำเนินการ";
            case 2:
                return "กำลังมีการประชุม";
            case -1:
                return "ประชุมเสร็จแล้ว";
                default:
                    return "การจอง ผิดพลาด";

        }
    }
}
