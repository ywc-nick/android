package com.example.project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.pojo.Custer;
import com.example.project.pojo.Text;
import com.example.project.sqlite.TextHistoryDao;
import com.example.project.sqlite.pojo.TextHistoryBean;
import com.example.project.util.ImageUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.example.project.util.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    private ImageView Im_autor, Im_like, Im_favorite;
    private TextView author, tv_title, tv_content, tatol_word, publish_time, likes_num, favorites_num;
    ScrollView scrollView;

    boolean isLiked = false; // 初始状态为未点赞
    boolean isFavorited = false; // 初始状态为未收藏

    Gson gson = new Gson();

    Text text;//获取传递过来的数据
    Integer tid, cid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Map map = SharedPreferencesUtils.getSharePreferences(getApplicationContext());
        cid = (Integer) map.get(SharedPreferencesUtils.ID);
        text = (Text) getIntent().getSerializableExtra("text");//获取传递过来的数据
        LoggerUtils.e("ArticleActivity================", text + "");
//        tid = getIntent().getIntExtra("tid", 0);//获取传递过来的数据
        if (text!=null){
            tid = text.getTid();
            setIm_like();
            init();
            fillData();
        }
    }



    public void init() {
        Im_autor = findViewById(R.id.im_autor);
        Im_like = findViewById(R.id.im_like);
        Im_favorite = findViewById(R.id.im_favorite);

        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tatol_word = findViewById(R.id.total_word);

        author = findViewById(R.id.tv_author);
        publish_time = findViewById(R.id.tv_publish_time);
        likes_num = findViewById(R.id.tv_likes_num);
        favorites_num = findViewById(R.id.tv_favorites_num);
        scrollView = findViewById(R.id.act_art_scrView);
        //点赞
        Im_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = likes_num.getText().toString();
                Integer nowNum = Integer.parseInt(num);
                if (isLiked) { // 如果已经被点赞，取消点赞
                    Im_like.setImageResource(R.drawable.like);
                    isLiked = false;
                    String fd_url = OkHttpUtil.baseUrl + "/like/delete/" + cid + "/" + tid;
                    delete(fd_url);
                    nowNum--;
                } else { // 如果未被点赞，添加点赞
                    String url = OkHttpUtil.baseUrl + "/like/add/" + cid + "/" + tid;
                    add(url);
                    Im_like.setImageResource(R.drawable.liked);
                    isLiked = true;
                    nowNum++;
                }
                likes_num.setText(nowNum+"");
            }
        });

        Im_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = favorites_num.getText().toString();
                Integer nowNum = Integer.parseInt(num);
                if (isFavorited) { // 如果已经被收藏，取消收藏
                    Im_favorite.setImageResource(R.drawable.favorite);
                    isFavorited = false;
                    String fd_url = OkHttpUtil.baseUrl + "/collect/delete/" + cid + "/" + tid;
                    delete(fd_url);
                    nowNum--;
                } else { // 如果未被收藏，添加收藏
                    Im_favorite.setImageResource(R.drawable.favorited);
                    isFavorited = true;
                    String url = OkHttpUtil.baseUrl + "/collect/add/" + cid + "/" + tid;
                    add(url);
                    nowNum++;
                }
                favorites_num.setText(nowNum+"");
            }
        });
//        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setIm_like();
    }

    private void delete(String url) {
        OkHttpUtil.delete(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ArticleActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(ArticleActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });
    }

    private void add(String url) {
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ArticleActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ArticleActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void fillData() {
        Im_autor.setImageBitmap(ImageUtils.getRoundedCornerBitmap(text.getCuster().getImagebytes(), ImageUtils.bigimage));
        //作者
        author.setText(text.getCuster().getVir_name());
        //题目
        tv_title.setText(text.getTheme());
        //内容
        tv_content.setText(text.getArticle());
        //文章总字数
        tatol_word.setText("总字数：" + text.getArticle().length());
        publish_time.setText("发布时间：" + TimeUtils.TimestampToDateString(text.getC_date()));
        //点赞数
        likes_num.setText(text.getKnums() + "");
        //收藏数
        favorites_num.setText(text.getCnums() + "");
    }




    //返回按钮

    public void btnReturn_detail(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在 post 方法中获取高度和宽度
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                // 在此处执行需要在 post 方法结束后执行的操作
//                int scrollWidth = scrollView.getWidth();
                int scrollHeight = scrollView.getHeight();

                // 获取 ScrollView 中的内容高度
                View contentView = scrollView.getChildAt(0);
                int contentHeight = contentView.getHeight();
                // 计算阅读进度
                double mReadRate ;
                if (contentHeight>scrollHeight){
                    mReadRate = (double)( scrollView.getScrollY() + scrollHeight) / contentHeight;
                }else {
                    mReadRate = (double)scrollHeight/ contentHeight;
                }

                TextHistoryDao textHistoryDao = new TextHistoryDao(getApplicationContext());
                // 创建DecimalFormat对象，保留2位小数
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                // 调用decimalFormat.format方法将保留2位小数后的数值转化为字符串
                String result = decimalFormat.format(mReadRate);
                double rate = Double.parseDouble(result) * 100;
                if (rate>=100){
                    rate=100;
                }
                textHistoryDao.insert(new TextHistoryBean(tid, tv_title.getText().toString(), (int) rate));
                Toast.makeText(ArticleActivity.this, "历史记录添加成功", Toast.LENGTH_SHORT).show();
                tid=0;
                text = null;
            }
        });
    }

    public void setIm_like() {
        String url = OkHttpUtil.baseUrl + "/text/coli/" + cid + "/" + tid;
        Gson gson = new Gson();
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Type type = new TypeToken<Map<String, Integer>>() {
                       }.getType();//指定合适的 Type 类型
                       Map<String, Integer> map = gson.fromJson(info, type);
                       Integer like = map.get("like");
                       Integer collect = map.get("collect");
                       if (like != 0) {
                           isLiked = true;
                           Im_like.setImageResource(R.drawable.liked);
                       }
                       if (collect != 0) {
                           isFavorited = true;
                           Im_favorite.setImageResource(R.drawable.favorited);
                       }
                   }
               });
            }
        });
    }
}

