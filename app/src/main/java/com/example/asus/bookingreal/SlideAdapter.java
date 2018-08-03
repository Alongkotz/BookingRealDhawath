package com.example.asus.bookingreal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_image = {
            R.drawable.sss,
            R.drawable.s3,
            R.drawable.s4,
            R.drawable.s5,
            R.drawable.s6,
            R.drawable.s7,
            R.drawable.s8,
            R.drawable.s9,
            R.drawable.s10,
            R.drawable.s11,
            R.drawable.s12
    };
    public String[] heading = {
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !",
            "คำแนะนำในการใช้งาน แอพลิเคชั่น !"
    };
    public String[] slide_des = {
            "เลือกการจองห้อง โดยกดไปที่ Icon การจองที่ชื่อว่า Booking",
            "จะเข้าสู่หน้าจอให้เลือกใช้ห้อง เราก็เลือกโดยการกดที่ตะกร้า",
            " ทำการเลือกรายการที่ต้องการ วันที่และเวลา และสิ่งที่ต้องการเพิ่มเติมต่างๆ",
            "หลังจากที่กดยืนยัน จะขึ้นหน้าให้ตรวจสอบว่าใช่หรือเปล่าถ้าใช่ก็ยืนยัน ถ้าไม่ก็ไปจองใหม่",
            " หลังจากจองเรียบร้อยแล้วให้ กดย้อนกลับที่ปุ่มโทรศัพท์ ไปหน้าจอหลักให้เลือกตะกร้าทางด้านขวาบน เพื่อที่จะยืนยัน",
            "พอกดเข้ามาแล้วจะขึ้นสิ่งที่ท่านได้จองไว้ ถ้าแน่ใจก็กดตกลง",
            "จะขึ้นหน้าต่างยืนยัน โดยให้ระบุแผนกของท่าน และถ้าท่านเปลี่ยนเบอร์โทรศัพท์โปรดระบุใหม่",
            "ถ้าต้องการตรวจสอบว่าที่จองไปได้รับการยืนยันจากเจ้าหน้าที่หรือยังให้เข้าไปตรวจสอบที่นี่เลย โดยกดที่รายการจอง",
            "ถ้ายังไม่ได้รับการยืนยันจะอยู่ที่หน้า NEW ",
            "แต่ถ้าได้รับการยืนยันแล้วจะอยู่ที่หน้า ยืนยันแล้ว",
            "สุดท้ายถ้าไม่อยากจองห้องซ้ำกับท่านอื่นก็ให้คลิกเข้าไปดูรายละเอียด ว่าวันไหนและเวลาไหนที่ถูกจองแล้ว"
    };

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.imageslide);
        TextView slideHeading = (TextView) view.findViewById(R.id.text);
        TextView slide_text = (TextView) view.findViewById(R.id.text_slide);

        slideImage.setImageResource(slide_image[position]);
        slideHeading.setText(heading[position]);
        slide_text.setText(slide_des[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
