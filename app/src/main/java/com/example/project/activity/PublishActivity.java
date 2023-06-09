package com.example.project.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class PublishActivity extends AppCompatActivity {

    private EditText et_topic;
    private EditText et_content;
    private Button btnPublish;

    OkHttpUtil okHttpUtil = new OkHttpUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        et_topic = findViewById(R.id.et_topic);
        et_content = findViewById(R.id.et_content);
        btnPublish = findViewById(R.id.btn_publish);

        //TODO 从共享首选项中拿取id
        //SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils();
        Map map = SharedPreferencesUtils.getSharePreferences(getApplicationContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theme = et_topic.getText().toString();
                String article = et_content.getText().toString();


                // 验证标题和正文是否为空
                if (TextUtils.isEmpty(theme)) {
                    Toast.makeText(getApplicationContext(), "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(article)) {
                    Toast.makeText(getApplicationContext(), "请输入正文", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 发表文章
                publishArticle(theme, article,id);
                Intent intent = new Intent( PublishActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void publishArticle(String theme, String article,int id) {
        // 将文章标题和正文添加到数据库

        String url = OkHttpUtil.baseUrl + "/text/add";  //添加方法的URL

//        Gson gson = new Gson();
//
//        String json = gson.toJson(theme);
//        String json1 = gson.toJson(article); // 将文章数据转换为JSON字符串

//        //类型
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
//        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), json1);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("theme",theme);
        formbody.add("article", article);
        formbody.add("custer_id", String.valueOf(id));

        //请求
        Request build = new Request.Builder().url(url).post(formbody.build()).build();

        //返回数据处理
        okHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PublishActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

//返回按钮

    public void btnReturn_detail(View view){

        Intent intent=new Intent(this, MainActivity.class);

        startActivity(intent);

    }

    }
