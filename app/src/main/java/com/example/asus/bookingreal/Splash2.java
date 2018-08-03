package com.example.asus.bookingreal;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash2 extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    Animation failling,up;
    ImageView ballon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        failling = AnimationUtils.loadAnimation(this,R.anim.falling);
        ballon = (ImageView)findViewById(R.id.ballon);
        ballon.setAnimation(failling);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(Splash2.this, Onboarding.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
