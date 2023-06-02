package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.adapter.HomeFraAdapter;
import com.google.android.material.tabs.TabLayout;


public class HomeFragment extends Fragment {

    View view;//容器视图

    TabLayout tableLayout;
    ViewPager viewPager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        initTabLayOrViewPager();
        return view;
    }

    private void init() {
        tableLayout = view.findViewById(R.id.fra_home_tablayout);
        viewPager = view.findViewById(R.id.fra_home_viewpager);
    }



    public void initTabLayOrViewPager(){
        //1.init

        tableLayout.addTab(tableLayout.newTab());//推荐
        tableLayout.addTab(tableLayout.newTab());//热门
        tableLayout.addTab(tableLayout.newTab());//排行榜
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        HomeFraAdapter homeFraAdapter = new HomeFraAdapter(getChildFragmentManager(), tableLayout.getTabCount());
        viewPager.setAdapter(homeFraAdapter);

        //2 关联
        tableLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}