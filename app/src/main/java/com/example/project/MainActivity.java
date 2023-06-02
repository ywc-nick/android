package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout home, my;

    //碎片管理器
    FragmentManager fm;
    FragmentTransaction ft;

    int posit;//标记上一次碎片的位置
    List<Fragment> fragmentsList;//碎片集合，不用每次都new，可替换旧的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //判断是不是再次进入
        savedInstanceStateInit(savedInstanceState);
        //初始进入首页


    }

    public void savedInstanceStateInit(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // 如果 savedInstanceState 为 null，说明是第一次创建，
            // 此时创建 Fragment 并添加到List集合中
            fragmentsList = new ArrayList<>();
            fragmentsList.add(new HomeFragment());
            fragmentsList.add(new MyFragment());
            replaceFragment(fragmentsList.get(0));
        } else {
            // 如果 savedInstanceState 不为 null，说明之前已经有一个 Fragment，
            // 此时根据 savedInstanceState 中保存的 Fragment 索引找到对应的 Fragment 并还原状态
            int position = savedInstanceState.getInt("position");
            replaceFragment(fragmentsList.get(position));
        }
    }

    public void init() {
        home = findViewById(R.id.act_main_home);
        my = findViewById(R.id.act_main_my);

        home.setOnClickListener(listener);
        my.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.act_main_home:
                    posit = 0;
                    replaceFragment(fragmentsList.get(posit));
                    break;
                case R.id.act_main_my:
                    posit = 1;
                    replaceFragment(fragmentsList.get(posit));
                    break;
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 保存当前 Fragment 索引
        outState.putInt("position", posit);
    }

    public void replaceFragment(Fragment fragment) {
        //1. 获取碎片管理器
        fm = getSupportFragmentManager();

        //2. 开启碎片事务
        ft = fm.beginTransaction();
        //3. 替换碎片
        ft.replace(R.id.act_main_blank_frag, fragment, String.valueOf(posit));
        //4. 提交事务
        ft.commit();
    }

}