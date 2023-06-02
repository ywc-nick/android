package com.example.project.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.project.fragment.home.HotFragment;
import com.example.project.fragment.home.RecommendFragment;
import com.example.project.fragment.home.TopListFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFraAdapter extends FragmentPagerAdapter {

    public static final String recommend = "推荐";
    public static final String hot = "热门";
    public static final String topList = "排行榜";
    int numOfTabs;
//    String[] title = new String[];
    List<String> title = new ArrayList();

    public HomeFraAdapter(@NonNull FragmentManager fm, int count) {
        super(fm);
        numOfTabs = count;
        title.add(recommend);
        title.add(hot);
        title.add(topList);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new RecommendFragment();
            case 1:
                return new HotFragment();
            case 2:
                return new TopListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  title.get(position);
    }
}
