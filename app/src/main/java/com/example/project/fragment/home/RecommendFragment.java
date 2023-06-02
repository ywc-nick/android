package com.example.project.fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
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

    public static final String recommend = "推荐";
    public static final String hot = "热门";
    public static final String topList = "排行";
    public static final String List1 = "排榜";
    public static final String topList2 = "排榜";
    public static final String topList3 = "行榜";
    public static final String topList4 = "排榜";


    public static List<String> list = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        init();
        test();
        initTabLayout();
        return view;
    }


    /**
     * 测试数据
     */
    private void test() {
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

        for (int i = 0; i < list.size(); i++) {
            tableLayout.addTab(tableLayout.newTab().setText(list.get(i)));
            LoggerUtils.i(list.get(i) + " " + i);
        }
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        replaceFragment(new KindFragment());

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(new KindFragment());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        //1. 获取碎片管理器
        fm = getChildFragmentManager();
        //2. 开启碎片事务
        ft = fm.beginTransaction();
        //3. 替换碎片
        ft.replace(R.id.fra_recommend_blank, fragment, null);
        //4. 提交事务
        ft.commit();
    }
}

