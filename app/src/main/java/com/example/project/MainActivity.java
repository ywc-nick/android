package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.project.activity.LoginActivity;
import com.example.project.activity.PublishActivity;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.MyFragment;
import com.example.project.util.DialogUtils;
import com.example.project.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout home, my;
    ImageView homeimage,myimage, publish;
    //碎片管理器
    FragmentManager fm;
    FragmentTransaction ft;

    int posit = 0;//标记上一次碎片的位置
    List<Fragment> fragmentsList;//碎片集合，不用每次都new，可替换旧的

//    // Todo
//    public void setTestData(){
//        SharedPreferencesUtils.setSharePreferences(getApplicationContext(),1,"12434","1242");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setData();
        //初始进入首页
    }
    public void setData() {

        fragmentsList = new ArrayList<>();
        fragmentsList.add(new HomeFragment());
        fragmentsList.add(new MyFragment());
        replaceFragment(fragmentsList.get(0));
        imageChange();
    }

    public void init() {
        homeimage = findViewById(R.id.act_main_home_image);
        myimage = findViewById(R.id.act_main_my_image);
        publish = findViewById(R.id.im_publish);
        home = findViewById(R.id.act_main_home);
        my = findViewById(R.id.act_main_my);

        publish.setOnClickListener(listener);
        home.setOnClickListener(listener);
        my.setOnClickListener(listener);
    }

    public void isLogin(){
        if (!SharedPreferencesUtils.isLogin(getApplicationContext())){
            DialogUtils.showConfirmDialog(this, null, "请先登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //判断是否登录
            switch (v.getId()) {
                case R.id.act_main_home:
                    posit = 0;
                    imageChange();
                    replaceFragment(fragmentsList.get(posit));
                    break;
                case R.id.act_main_my:
                    if (SharedPreferencesUtils.isLogin(getApplicationContext())){
                        posit = 1;
                        imageChange();
                        replaceFragment(fragmentsList.get(posit));
                    }else {
                        isLogin();
                    }
                    break;
                case R.id.im_publish:
                    if (SharedPreferencesUtils.isLogin(getApplicationContext())){
                        Intent intent=new Intent(MainActivity.this, PublishActivity.class);
                        startActivity(intent);
                    }else {
                        isLogin();
                    }
                    break;
            }
        }
    };

    public void imageChange(){
        if (posit==0){
            homeimage.setImageResource(R.drawable.homeselected);
            myimage.setImageResource(R.drawable.my);
        }else {
            homeimage.setImageResource(R.drawable.home);
            myimage.setImageResource(R.drawable.myselected);
        }


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