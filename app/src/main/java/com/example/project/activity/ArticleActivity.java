package com.example.project.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.sqlite.TextMethod;
import com.example.project.pojo.Custer;
import com.example.project.pojo.Text;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ArticleActivity extends AppCompatActivity {

    SQLiteOpenHelper sqLiteDbHelper;

    OkHttpUtil okHttpUtil = new OkHttpUtil();

    private ImageView Im_save, Im_like, Im_favorite;
    private EditText et_title, et_content;
    private TextView author, publish_time, likes_num, favorites_num;

    boolean isLiked = false; // 初始状态为未点赞
    boolean isFavorited = false; // 初始状态为未收藏

    private String url = "http://10.0.2.2:8080/qianxun/app";

    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        init();
        getInformation();
        save();
    }

        public void init(){
            Im_save = findViewById(R.id.im_save);
            Im_like = findViewById(R.id.im_like);
            Im_favorite = findViewById(R.id.im_favorite);

            et_title = findViewById(R.id.et_title);
            et_content = findViewById(R.id.et_content);

            author = findViewById(R.id.tv_author);
            publish_time = findViewById(R.id.tv_publish_time);
            likes_num = findViewById(R.id.tv_likes_num);
            favorites_num = findViewById(R.id.tv_favorites_num);

            Im_save.setOnClickListener((View.OnClickListener) this);
            Im_like.setOnClickListener((View.OnClickListener) this);
            Im_favorite.setOnClickListener((View.OnClickListener) this);

        }

        public void onClick(View v) {
            switch (v.getId()){
                case R.id.im_save:
                    save();
                    break;
                case R.id.im_like:
                    like();
                    break;
                case R.id.im_favorite:
                    favorite();
                    break;
            }
        }

    private void like() {
        if (isLiked) { // 如果已经被点赞，取消点赞
            Im_like.setImageResource(R.drawable.like);
            isLiked = false;

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            Intent intent=getIntent();
            String id=intent.getStringExtra("id");
            String l_url = OkHttpUtil.baseUrl + ""; //删除点赞的url


            FormBody.Builder formbody = new FormBody.Builder();
            formbody.add("id",id);
            //请求
            Request build = new Request.Builder().url(l_url).post(formbody.build()).build();

            //返回数据处理
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ArticleActivity.this, "取消点赞失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ArticleActivity.this, "已取消点赞", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        } else { // 如果未被点赞，添加点赞
            Im_favorite.setImageResource(R.drawable.liked);
            isLiked = true;
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            Intent intent=getIntent();
            String id=intent.getStringExtra("id");
            String add_url = OkHttpUtil.baseUrl + ""; //添加点赞的url


            FormBody.Builder formbody = new FormBody.Builder();
            formbody.add("id",id);
            //请求
            Request build = new Request.Builder().url(add_url).post(formbody.build()).build();

            //返回数据处理
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(ArticleActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Toast.makeText(ArticleActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void favorite() {
        if (isFavorited) { // 如果已经被收藏，取消收藏
            Im_favorite.setImageResource(R.drawable.favorite);
            isFavorited = false;

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            Intent intent=getIntent();
            String id=intent.getStringExtra("id");
            String l_url = OkHttpUtil.baseUrl + ""; //删除收藏的url


            FormBody.Builder formbody = new FormBody.Builder();
            formbody.add("id",id);
            //请求
            Request build = new Request.Builder().url(l_url).post(formbody.build()).build();

            //返回数据处理
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(ArticleActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Toast.makeText(ArticleActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
            });
        } else { // 如果未被收藏，添加收藏
            Im_favorite.setImageResource(R.drawable.favorited);
            isFavorited = true;

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            Intent intent=getIntent();
            String id=intent.getStringExtra("id");
            String l_url = OkHttpUtil.baseUrl + ""; //添加收藏的url


            FormBody.Builder formbody = new FormBody.Builder();
            formbody.add("id",id);
            //请求
            Request build = new Request.Builder().url(l_url).post(formbody.build()).build();

            //返回数据处理
            okHttpClient.newCall(build).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(ArticleActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Toast.makeText(ArticleActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getInformation(){
        //将text表中的相关字段查询出来显示在界面上
//        Intent intent=getIntent();
//        String id=intent.getStringExtra("id");
        Map map = SharedPreferencesUtils.getSharePreferences(getApplicationContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);


        if (id == 0){
            return;
        }
        url =OkHttpUtil.baseUrl+"/custer/" + id;
        OkHttpUtil.get(url, new Callback(){
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
//                LoggerUtils.i("get data success",info);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Text text = gson.fromJson(info,Text.class);
                        //textView.setText(custer.getNums().toString());
                        author.setText(text.getCuster().toString());
                        et_title.setText(text.getTheme().toString());
                        et_content.setText(text.getArticle().toString());
                        likes_num.setText(text.getKnums());
                        favorites_num.setText(text.getCnums());
                    }
                });
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }
        });
        }




    //保存到手机
    public void save() {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();

        TextMethod textMethod = new TextMethod(db);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");// HH:mm:ss

        Date date = new Date(System.currentTimeMillis());


        String dates = simpleDateFormat.format(date);

        String title = et_title.getText().toString();

        String context = et_content.getText().toString();

        Text text = new Text();

        text.setC_date(dates);

        text.setTheme(title);

        text.setArticle(context);

        textMethod.save(text);

        Toast.makeText(ArticleActivity.this, "保存成功！", Toast.LENGTH_LONG).show();


    }


    //返回按钮

    public void btnReturn_detail(View view){

        Intent intent=new Intent(this, MainActivity.class);

        startActivity(intent);

    }
}
