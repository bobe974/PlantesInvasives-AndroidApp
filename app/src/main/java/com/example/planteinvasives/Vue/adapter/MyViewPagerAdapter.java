package com.example.planteinvasives.Vue.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.planteinvasives.Vue.Fragment.InfoFragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    InfoFragment page1, page2, page3;
    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        //initialise les fragments
        page1 = new InfoFragment();
        page1.setCurpos(1);

        page2 = new InfoFragment();
        page2.setCurpos(2);

        page3 = new InfoFragment();
        page3.setCurpos(3);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return page1;
            case 1:
                return page2;
            case 2:
                return page3;

            default:return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
