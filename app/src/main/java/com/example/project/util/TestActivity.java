package com.example.project.util;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void test(){
        OkHttpUtil.get("https://example.com", new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败情况
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 处理请求成功情况
                String responseBody = response.body().string();
                // ...
            }
        });
    }
}