package com.example.weatherforecasts;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewParent;

import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String[] CITY_LIST = {
            "270000",
            "130010",
            "040010",
    };

    private List<String> pointList;

    private  class Adapter extends FragmentPagerAdapter {

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ForecastFragment.newInstance((pointList.get(position)));
        }

        @Override
        public  int getCount() {
            return pointList.size();
        }
    }

    private Adapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (pointList == null) {
            pointList = Arrays.asList(CITY_LIST);
        }

        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}