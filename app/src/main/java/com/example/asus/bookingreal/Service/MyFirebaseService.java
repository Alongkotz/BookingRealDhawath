package com.example.asus.bookingreal.Service;

import android.util.Log;

import com.example.asus.bookingreal.Retrofit.IBookingAPI;
import com.example.asus.bookingreal.Util.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        if (Common.cUser != null)
            updateTokenToFirebase();
    }

    private void updateTokenToFirebase() {
        IBookingAPI mService = Common.getAPI();
        mService.updateToken(Common.cUser.getPhone(), FirebaseInstanceId.getInstance().getToken(),"0").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("DEBUG", response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("DEBUG", t.getMessage());
            }
        });
    }
}
