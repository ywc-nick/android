package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.project.R;
import com.example.project.activity.SearchActivity;
import com.example.project.adapter.MyFraAdapter;
import com.example.project.fragment.home.HotFragment;
import com.example.project.fragment.home.RecommendFragment;
import com.example.project.fragment.home.TopListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {



    public static final String recommend = "推荐";
    public static final String hot = "热门";
//    public static final String topList = "排行榜";
    List<String> titles;
    List<Fragment> fragments;

    View view;//容器视图

    TabLayout tableLayout;
    ViewPager viewPager;

    FrameLayout frameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        initData();
        initTabLayOrViewPager();
        return view;
    }

    private void init() {
        tableLayout = view.findViewById(R.id.fra_home_tablayout);
        viewPager = view.findViewById(R.id.fra_home_viewpager);
        frameLayout = view.findViewById(R.id.fra_search_frame);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    public void initData(){
        fragments = new ArrayList<>();
        fragments.add(new HotFragment());//热门
        fragments.add(new RecommendFragment());//推荐
//        fragments.add(new TopListFragment());//排行榜
        titles = new ArrayList<>();
        titles.add(hot);
        titles.add(recommend);
//        titles.add(topList);
    }


    public void initTabLayOrViewPager() {
        //1.init
        tableLayout.addTab(tableLayout.newTab());//热门
        tableLayout.addTab(tableLayout.newTab());//推荐
//        tableLayout.addTab(tableLayout.newTab());//排行榜
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        MyFraAdapter homeFraAdapter = new MyFraAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(homeFraAdapter);
        viewPager.setCurrentItem(1);//设置第二个碎片为初始碎片
        viewPager.setOffscreenPageLimit(2);//设置预加载页面数量的方法

        //2 关联
        tableLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
//        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition(), true);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

}