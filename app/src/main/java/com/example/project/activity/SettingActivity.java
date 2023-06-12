package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.project.R;
import com.example.project.pojo.Text;
import com.example.project.util.LoggerUtils;

public class SettingActivity extends AppCompatActivity {
    ImageView manage,phonenumber,safe;
    TextView Logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
    }


//    public void init(View view){
//        manage = view.findViewById(R.id.act_my_text_entry1);
//        phonenumber = view.findViewById(R.id.act_my_text_entry2);
//        safe = view.findViewById(R.id.act_my_text_entry3);
//        Logout = view.findViewById(R.id.LogOut);
//    }
//
//    View.OnClickListener listener = new View.OnClickListener() {
//        Intent intent;
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.act_my_text_entry1:
//                    intent = new Intent(,RegisterActivity.class);//跳转到登录页面
//                    break;
//                case R.id.act_my_text_entry2:
//                    intent = new Intent(,);//直接跳转到个人信息修该页面
//                case R.id.act_my_text_entry3:
//                    intent = new Intent(,);//跳转到修改密码页面
//                case R.id.LogOut:
//                    intent = new Intent(SettingActivity.this,LoginActivity.class);//跳转到登录页面
//            }
//        }
//    };


}
