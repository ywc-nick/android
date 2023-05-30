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

public class MainActivity extends AppCompatActivity {

    LinearLayout home,my;

    //碎片管理器
    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //初始进入首页
        replaceFragment(new HomeFragment());
    }

    public void init(){
        home = findViewById(R.id.act_main_home);
        my = findViewById(R.id.act_main_my);

        home.setOnClickListener(listener);
        my.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.act_main_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.act_main_my:
                    replaceFragment(new MyFragment());
                    break;
            }
        }
    };

    public void replaceFragment(Fragment fragment){
        //1. 获取碎片管理器
        fm = getSupportFragmentManager();
        //2. 开启碎片事务
        ft = fm.beginTransaction();
        //3. 替换碎片
        ft.replace(R.id.act_main_blank_frag,fragment,null);
        //4. 提交事务
        ft.commit();
    }
}