package org.hr24.almel.careertraectory;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button registerButton;
    LinearLayout geoLinLay, calendarLinLay;
    ImageSwitcher mImageSwitcher;
    private int position;
    private int[] mImageIds = { R.drawable.main, R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.fourth};
    private static Handler h;
    Thread t;
    Animation slideInLeftAnimation;
    Animation slideOutRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton = (Button) findViewById(R.id.register_btn);
        geoLinLay = (LinearLayout) findViewById(R.id.geo_ll);
        calendarLinLay = (LinearLayout) findViewById(R.id.calendar_ll);
        mImageSwitcher = (ImageSwitcher)findViewById(R.id.imageSwitcher);


        registerButton.setOnClickListener(this);
        geoLinLay.setOnClickListener(this);
        calendarLinLay.setOnClickListener(this);
        mImageSwitcher.setOnClickListener(this);

        slideInLeftAnimation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slideOutRight = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        mImageSwitcher.setInAnimation(slideInLeftAnimation);
        mImageSwitcher.setOutAnimation(slideOutRight);

        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {

                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                ImageSwitcher.LayoutParams params = new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                imageView.setLayoutParams(params);
                return imageView;
            }
        });

        position = 0;



        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (position == mImageIds.length - 1) {
                    position = 0;
                    mImageSwitcher.setImageResource(mImageIds[position]);
                } else {
                    mImageSwitcher.setImageResource(mImageIds[++position]);
                }
            }
        };


        t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        h.sendEmptyMessage(1);
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacksAndMessages(null);
    }



    @Override
    public void onClick(View view) {

        Intent intentRegister = new Intent(MainActivity.this, Register.class);

        switch (view.getId()){
            case R.id.register_btn:
                if (NetworkStatusChecker.isNetworkAvailable(this)){
                startActivity(intentRegister);
                } else {
                    Toast.makeText(MainActivity.this, "Сеть недоступна, попробуйте позже", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.geo_ll:
                double latitude = 55.829677;
                double longitude = 37.449007;
                String label = "Карьерная траектория";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent geoIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(geoIntent);

                break;
            case R.id.calendar_ll:
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2016, 11, 12, 19, 0);
                Calendar endTime = Calendar.getInstance();
                endTime.set(2012, 11, 12, 20, 0);
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Карьерная траектория")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Конференция")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, getResources().getString(R.string.geo))
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(calendarIntent);

                break;

        }
    }


}
