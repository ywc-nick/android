package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.pojo.Custer;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    private EditText name, password;
    ImageView back;
    Gson gson = new Gson();
    Custer custer;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);
        name = findViewById(R.id.edt_name);
        password = findViewById(R.id.edt_psw);

        //登录界面跳转到主界面
        back = findViewById(R.id.act_login_back);
        login.setOnClickListener(listener);
        back.setOnClickListener(listener);
        // 点击注册按钮跳转到注册界面
        register.setOnClickListener(listener);
        //注册跳转
        name.setText(getIntent().getStringExtra("username"));

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_login:

                    Custer custer = login();
                    if (custer != null) {
                        SharedPreferencesUtils.setSharePreferences(getBaseContext(), custer.getId(), custer.getUsername(), custer.getPassword());
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.btn_register:
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.act_login_back:
                    finish();
                    break;
            }
        }
    };



    private Custer login() {
        //获取EditText数据
        String username = name.getText().toString();
        String pass = password.getText().toString();




            url = OkHttpUtil.baseUrl + "/custer/login/" + username + "/" + pass;
        //2.配置request:添加数据+请求方法+url
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoggerUtils.i("数据获取失败！");
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            custer = gson.fromJson(info, Custer.class);
                        } catch (Exception e) {
                            LoggerUtils.e("登陆失败", e.getMessage());
                        }
                    }
                });
            }
        });
        return custer;
    }
}

