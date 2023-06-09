package com.example.project.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.ArticleActivity;
import com.example.project.adapter.TextAdapter;
import com.example.project.pojo.Text;
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


public class KindFragment extends Fragment implements TextAdapter.ItemClickInterface{

    RecyclerView recyclerView;
    TextAdapter adapter;
    List<Text> texts;
    Integer kid;//每个类型的id
    int currentPage;

    KindInterface kindInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kid = getArguments().getInt("kind");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        texts = new ArrayList<>();
        currentPage =1;
        View view =  inflater.inflate(R.layout.fragment_kind, container, false);
        init(view);
        requestData();
        return view;
    }


    Handler mHandler =new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!texts.isEmpty()){
                        adapter.updateData(texts);
                        adapter.notifyDataSetChanged();
                        LoggerUtils.i("+KindFragment+"+kid, texts.toString());
                    }else {
                        Toast.makeText(getActivity(), "已经到底了！！！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void init(View view) {
        recyclerView = view.findViewById(R.id.fra_kind_recy);
        adapter = new TextAdapter(getActivity(),texts,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
//        kindInterface.sendToParent(kid,texts);
        Gson gson = new Gson();
        String url = OkHttpUtil.baseUrl+"/text/kind/"+kid+ "/"+ currentPage;
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
                            texts= gson.fromJson(info, type);
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



    @Override
    public void onDestroy() {
        super.onDestroy();
        texts.clear();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onItemClick(int position, Text text) {
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra("text",text);
        getActivity().startActivity(intent);
    }

    public interface KindInterface {
        void sendToParent(int kid, List<Text> textsData);
    }
}