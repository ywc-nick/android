package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapter.TextAdapter;
import com.example.project.pojo.Text;
import com.example.project.util.DialogUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchResultActivity extends AppCompatActivity implements TextAdapter.ItemClickInterface{

    ImageView back;
    TextView searchText;
    RecyclerView recyclerView;

    TextAdapter adapter;
    private List<Text> dataList =new ArrayList<>() ;
    private int currentPage = 1;
    String result;//搜索结果内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        init();
        // 获取携带搜索结果的 Intent 实例
        result = getIntent().getStringExtra("result");
        searchText.setText(result);

    }

    @Override
    protected void onStart() {
        super.onStart();
        requestData();
    }

    public void init(){
        back = findViewById(R.id.iv_search_res_back_image);
        searchText = findViewById(R.id.iv_search_res_back_text);
        recyclerView = findViewById(R.id.act_search_recview);

        back.setOnClickListener(listener);
        searchText.setOnClickListener(listener);

        adapter = new TextAdapter(this,dataList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int itemCount = linearLayoutManager.getItemCount();
                    if (lastVisiblePosition == (itemCount - 1)) {
                        requestData();
                    }
                }
            }
        });

    }

    private void requestData() {
        Gson gson = new Gson();
        String url = OkHttpUtil.baseUrl+"/text/search/"+result+ "/"+ currentPage;
        OkHttpUtil.get(url, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        LoggerUtils.i("数据获取失败！");
                        Message errorMessage = mHandler.obtainMessage(0);
                        mHandler.sendMessage(errorMessage);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String info = response.body().string();
                        Type type = new TypeToken<List<Text>>() {}.getType();//指定合适的 Type 类型
                        try {
                            dataList= gson.fromJson(info, type);
                            Message successMessage = mHandler.obtainMessage(1);
                            currentPage++;
                            mHandler.sendMessage(successMessage);
                            // 在此处理 data 对象
                        } catch (Exception e) {
                            // 处理 JsonSyntaxException 异常
                            LoggerUtils.e( "Failed to parse JSON string", e.toString());
                        }

                    }
                }

        );
    }

    Handler mHandler =new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!dataList.isEmpty()){
                        adapter.updateData(dataList);
                        adapter.notifyDataSetChanged();
                        LoggerUtils.i("+SearchResultActivity", dataList.toString());
                    }else {
                        Toast.makeText(getApplication(), "已经到底了！！！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_search_res_back_image:
                    finish();
                    break;
                case R.id.iv_search_res_back_text:
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onItemClick(int position, Text text) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("text",text);
        startActivity(intent);
    }
}