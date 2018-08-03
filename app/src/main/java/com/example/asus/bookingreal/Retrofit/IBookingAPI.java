package com.example.asus.bookingreal.Retrofit;


import com.example.asus.bookingreal.Model.Banner;
import com.example.asus.bookingreal.Model.Category;
import com.example.asus.bookingreal.Model.CheckUserResponse;
import com.example.asus.bookingreal.Model.Order;
import com.example.asus.bookingreal.Model.Room;
import com.example.asus.bookingreal.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import io.reactivex.Observable;

public interface IBookingAPI {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("cname") String cname,
                               @Field("bname") String bname);

    @FormUrlEncoded
    @POST("getroom.php")
    Observable<List<Room>> getRoom(@Field("menuid") String menuID);


    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone") String phone
    );

    @GET("getbanner.php")
    Observable<List<Banner>> getBanner();

    @GET("getmenu.php")
    Observable<List<Category>> getMenu();

    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("summitorder.php")
    Call<String> summitOrder(@Field("phone") String phone,
                             @Field("orderDetail") String orderDetail,
                             @Field("comment") String comment,
                             @Field("subject") String subject);

    @FormUrlEncoded
    @POST("getorder.php")
    Observable<List<Order>> getOrderByStatus(@Field("status") String status);

    @FormUrlEncoded
    @POST("updatetoken.php")
    Call<String> updateToken(@Field("phone") String phone,
                             @Field("token") String token,
                             @Field("isServerToken") String isServerToken);

    @FormUrlEncoded
    @POST("cancelorder.php")
    Call<String> CancelOrder(@Field("orderId") String orderId,
                             @Field("userPhone") String userPhone);
}
