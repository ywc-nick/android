package com.example.project.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.pojo.Kind;
import com.example.project.util.ImageUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
    private TextView words_num;
    private Button btnPublish;
    private Spinner spinner;
    Gson gson = new Gson();
    List<String> kinds = new ArrayList<>();
    List<Kind> titles;
    Integer kid;
    // 从后端获取kind表数据
//     kinds;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    for (Kind kind : titles) {
                        kinds.add(kind.getContent());
                    }
                    // 在此处理 data 对象
                    if (!kinds.isEmpty()) {
                        // 创建一个Adapter将数据绑定到Spinner
                        adapter = new ArrayAdapter<>(PublishActivity.this, android.R.layout.simple_spinner_item, kinds);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // 设置Adapter
                        spinner.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        //处理数据为null的情况
                        Toast.makeText(getApplicationContext(), "无法选择文章类型", Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:
                    break;
            }
        }
    };

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        et_topic = findViewById(R.id.et_topic);
        et_content = findViewById(R.id.et_content);
        words_num = findViewById(R.id.words_num);
        btnPublish = findViewById(R.id.btn_publish);
        spinner = findViewById(R.id.kindSpinner);

        Map map = SharedPreferencesUtils.getSharePreferences(getApplicationContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);
        requestData();
        //输入字数显示
        int maxWords = 1000; // 最大字数限制
        et_content.addTextChangedListener(new TextWatcher() {
            int totalWords = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalWords = s.length();
                if (totalWords > maxWords) {
                    et_content.setError("字数已超过限制");
                } else {
                    words_num.setText("已输入" + totalWords + "字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kid = position+1;
                LoggerUtils.i(kid + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 从共享首选项中拿取id

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
                publishArticle(theme, article, id, kid);
                Intent intent = new Intent(PublishActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void requestData() {

        String url = OkHttpUtil.baseUrl + "/other/kind";
        OkHttpUtil.get(url, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        LoggerUtils.i("数据获取失败！");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String info = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Type type = new TypeToken<List<Kind>>() {
                                }.getType();//指定合适的 Type 类型

                                try {
                                    titles = gson.fromJson(info, type);
                                    Message message = mHandler.obtainMessage(1);
                                    mHandler.sendMessage(message);
                                } catch (Exception e) {
                                    // 处理 JsonSyntaxException 异常
                                    LoggerUtils.e("Failed to parse JSON string", e.toString());
                                }
                            }
                        });

                    }
                }

        );

    }


    private void publishArticle(String theme, String article, int id, Integer kid) {
        // 将文章标题和正文添加到数据库

        String url = OkHttpUtil.baseUrl + "/text/add";  //添加方法的URL


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
//        @RequestParam("kind") String kind,@RequestParam("Theme")String Theme,
//        @RequestParam("article")String article,@RequestParam("cid")int cid

        FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("theme", theme);
        formbody.add("kid", String.valueOf(kid));
        formbody.add("article", article);
        formbody.add("cid", String.valueOf(id));

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

    public void btnReturn_detail(View view) {

        finish();

    }

}
