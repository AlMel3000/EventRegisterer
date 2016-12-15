package org.hr24.almel.careertraectory;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.hr24.almel.careertraectory.utils.DepthPageTransformer;
import org.hr24.almel.careertraectory.utils.NetworkStatusChecker;
import org.hr24.almel.careertraectory.utils.SamplePagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button registerButton;
    LinearLayout geoLinLay, calendarLinLay;
    ViewPager mImageSwitcher;
    private int position;
    private static Handler h;
    private static Runnable runnable;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton = (Button) findViewById(R.id.register_btn);
        geoLinLay = (LinearLayout) findViewById(R.id.geo_ll);
        calendarLinLay = (LinearLayout) findViewById(R.id.calendar_ll);
        mImageSwitcher = (ViewPager) findViewById(R.id.imageSwitcher);

        mImageSwitcher.setPageTransformer(true, new DepthPageTransformer());


        registerButton.setOnClickListener(this);
        geoLinLay.setOnClickListener(this);
        calendarLinLay.setOnClickListener(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<View>();

        View page = inflater.inflate(R.layout.page, null);
        ImageView imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.main);
        pages.add(page);

        page = inflater.inflate(R.layout.page, null);
        imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.first);
        pages.add(page);

        page = inflater.inflate(R.layout.page, null);
        imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.second);
        pages.add(page);

        page = inflater.inflate(R.layout.page, null);
        imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.third);
        pages.add(page);

        page = inflater.inflate(R.layout.page, null);
        imageView = (ImageView) page.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.fourth);
        pages.add(page);


        SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(pages);

        mImageSwitcher.setAdapter(pagerAdapter);
        mImageSwitcher.setCurrentItem(0);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.c_indicator);
        indicator.setViewPager(mImageSwitcher);

        mImageSwitcher.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        h= new Handler();
        runnable = new Runnable() {
            public void run() {
                if( position >= 4){
                    position = 0;
                }else{
                    position = position+1;
                }
                mImageSwitcher.setCurrentItem(position, true);
                h.postDelayed(runnable, 3000);
            }
        };

        mImageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (h!= null) {
                    h.removeCallbacks(runnable);
                }
                return false;
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        if (h!= null) {
            h.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        h.postDelayed(runnable, 3000);
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
                try {
                    startActivity(geoIntent);
                }catch (ActivityNotFoundException e) {
                      Toast.makeText(this,
                    "Карты не установлены",
                    Toast.LENGTH_SHORT).show();
                }


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
                try {
                    startActivity(calendarIntent);
                }catch (ActivityNotFoundException e) {
                    Toast.makeText(this,
                            "Календарь не установлен",
                            Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }


}
