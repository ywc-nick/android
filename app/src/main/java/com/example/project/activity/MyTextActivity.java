package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project.R;
import com.example.project.adapter.MyFraAdapter;
import com.example.project.fragment.MyFragment;
import com.example.project.fragment.mytext.CollectFragment;
import com.example.project.fragment.mytext.LikeFragment;
import com.example.project.fragment.mytext.TextFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MyTextActivity extends AppCompatActivity {

    ImageView back;
    TabLayout tabLayout;
    ViewPager viewPager;

    public static final String my = "我的";
    public static final String like = "喜欢";
    public static final String collect = "收藏";
    List<String> titles;
    List<Fragment> fragments;

    int fra_index; //my:1  like:0 collect: 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_text);
        init();
        setData();
        PagerAndTab();
    }

    private void setData() {
        titles = new ArrayList<>();
        titles.add(like);
        titles.add(my);
        titles.add(collect);
        fragments = new ArrayList<>();
        fragments.add(new LikeFragment());
        fragments.add(new TextFragment());
        fragments.add(new CollectFragment());

        //判断进入那个碎片
        fra_index = getIntent().getIntExtra(MyFragment.opt,0);

    }

    public void init() {

        back = findViewById(R.id.act_my_text_back);
        tabLayout = findViewById(R.id.act_my_text_tablayout);
        viewPager = findViewById(R.id.act_my_text_viewpager);

        back.setOnClickListener(listener);

    }

    public void PagerAndTab() {

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        MyFraAdapter adapter = new MyFraAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(fra_index);
        viewPager.setOffscreenPageLimit(3);//设置预加载页面数量的方法
        //关联
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.act_my_text_back:
                    finish();
                    break;
            }
        }
    };


}