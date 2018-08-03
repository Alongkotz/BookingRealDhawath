package com.example.asus.bookingreal;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Onboarding extends AppCompatActivity {
    private ViewPager SlideViewPaper;
    private LinearLayout Dotlayout;

    private TextView[] mdot;
    private SlideAdapter slideAdapter;
    private int mCurrentPage;
    private Button nextbtn, prevbtn,skip_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        SlideViewPaper = (ViewPager) findViewById(R.id.slidelayout);
        Dotlayout = (LinearLayout) findViewById(R.id.linear);

        nextbtn = (Button) findViewById(R.id.cont);
        prevbtn = (Button) findViewById(R.id.prev);
        skip_btn = (Button) findViewById(R.id.skip_btn);
        slideAdapter = new SlideAdapter(this);

        SlideViewPaper.setAdapter(slideAdapter);

        addDotsIndicator(0);

        SlideViewPaper.addOnPageChangeListener(viewListener);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideViewPaper.setCurrentItem(mCurrentPage + 1);
            }
        });
        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideViewPaper.setCurrentItem(mCurrentPage - 1);
            }
        });
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Onboarding.this,MainActivity.class));
            }
        });
    }

    public void addDotsIndicator(int position) {
        mdot = new TextView[11];
        Dotlayout.removeAllViews();
        for (int i = 0; i < mdot.length; i++) {
            mdot[i] = new TextView(this);
            mdot[i].setText(Html.fromHtml("&#8226;"));
            mdot[i].setTextSize(35);
            mdot[i].setTextColor(getResources().getColor(R.color.colortranspentWhite));

            Dotlayout.addView(mdot[i]);
        }
        if (mdot.length > 0) {
            mdot[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }


    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;

            if (position == 0) {
                nextbtn.setEnabled(true);
                skip_btn.setEnabled(true);
                prevbtn.setEnabled(false);
                prevbtn.setVisibility(View.INVISIBLE);

                nextbtn.setText("ต่อไป");
                prevbtn.setText("");
            } else if (position == mdot.length - 1) {
                nextbtn.setEnabled(true);
                prevbtn.setEnabled(true);
                prevbtn.setVisibility(View.VISIBLE);

                nextbtn.setText("จบ");
                prevbtn.setText("ย้อนกลับ");
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Onboarding.this,MainActivity.class));
                    }
                });

            } else {
                nextbtn.setEnabled(true);
                prevbtn.setEnabled(true);
                prevbtn.setVisibility(View.VISIBLE);

                nextbtn.setText("ต่อไป");
                prevbtn.setText("ย้อนกลับ");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
