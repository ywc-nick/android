package com.example.project.fragment.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.MyFragment;
import com.example.project.util.LoggerUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class RecommendFragment extends Fragment {


    View view;//容器视图
    TabLayout tableLayout;


    //碎片管理器
    FragmentManager fm;
    FragmentTransaction ft;
    int posit;//碎片和tablayout的位置


    public static List<String> list;
    List<Fragment> fragmentsList;//碎片集合，不用每次都new，可替换旧的

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        init();
        test();
        setData();
        initTabLayout();
        return view;
    }

    public void setData() {

        fragmentsList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            fragmentsList.add(new KindFragment());
        }
        replaceFragment(fragmentsList.get(0));

    }

    /**
     * 测试数据
     */
    private void test() {
        list = new ArrayList();
        String recommend = "推荐";
        String hot = "热门";
        String topList = "排行";
        String List1 = "排榜";
        String topList2 = "排榜";
        String topList3 = "行榜";
        String topList4 = "排榜";

        list.add(recommend);
        list.add(hot);
        list.add(topList);
        list.add(List1);
        list.add(topList2);
        list.add(topList4);
        list.add(topList3);
        list.add(topList3);
        list.add(topList3);
        list.add(topList3);
    }

    private void init() {
        tableLayout = view.findViewById(R.id.fra_recommend_tablayout);
    }


    public void initTabLayout() {

        LoggerUtils.i(list.size() + "");
        for (int i = 0; i < list.size(); i++) {
            tableLayout.addTab(tableLayout.newTab().setText(list.get(i)).setTag(i));//tag设为下标
            LoggerUtils.i(list.get(i) + " " + i);
        }
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                posit = (Integer) tab.getTag();
                replaceFragment(fragmentsList.get(posit));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前 Fragment 索引
        outState.putInt("position", posit);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoggerUtils.e("RecommendFragment销毁");
    }

    public void replaceFragment(Fragment fragment) {
        //1. 获取碎片管理器
        fm = getChildFragmentManager();
        //2. 开启碎片事务
        ft = fm.beginTransaction();

        //3. 替换碎片
        ft.replace(R.id.fra_recommend_blank, fragment);
        LoggerUtils.i("碎片已替换");

        //4. 提交事务
        ft.commit();
    }
}

