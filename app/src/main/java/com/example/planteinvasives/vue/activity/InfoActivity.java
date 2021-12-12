package com.example.planteinvasives.vue.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.planteinvasives.R;
import com.example.planteinvasives.vue.adapter.MyViewPagerAdapter;


public class InfoActivity extends AppCompatActivity {

    static ViewPager pager;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        pager = findViewById(R.id.vpMain);
        pager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        //page en plein ecran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public static void  next_fragment(View view) {
        pager.setCurrentItem(pager.getCurrentItem()+1);
    }
}
