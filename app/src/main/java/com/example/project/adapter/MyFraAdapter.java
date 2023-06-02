package com.example.project.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.project.fragment.home.HotFragment;
import com.example.project.fragment.home.RecommendFragment;
import com.example.project.fragment.home.TopListFragment;
import com.example.project.util.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

public class MyFraAdapter extends FragmentPagerAdapter {


    List<String> titles;

    List<Fragment> fragments;

    public MyFraAdapter(@NonNull FragmentManager fm, List<Fragment> frags, List<String> tit) {
        super(fm);
        fragments = frags;
        titles = tit;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        LoggerUtils.i("HomeFraAdapter碎片位置", position + "");
        return fragments.get(position);

    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }
}
