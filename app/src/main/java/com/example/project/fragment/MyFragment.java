package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.pojo.Custer;
import com.example.project.util.DialogUtils;
import com.example.project.util.MyLogger;
import com.example.project.util.OkHttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MyFragment extends Fragment {


    ImageView image;//头像
    TextView nickname;//昵称

    private String url = "http://10.0.2.2:8080/qianxun/app";

    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init();
        return inflater.inflate(R.layout.fragment_my, container, false);
    }


    public void init(){
        image = getActivity().findViewById(R.id.fra_my_image);
        nickname = getActivity().findViewById(R.id.fra_my_nickname);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO 从共享首选项中拿取id
        Integer id = null;
        url = "http://10.0.2.2:8080/qianxun/app/my/info/" + id;

        OkHttpUtil.get(url, new Callback(){

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
                MyLogger.i("get data",info);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Custer custer = gson.fromJson(info,Custer.class);


                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MyLogger.i("数据获取失败！");
            }
        });
    }
}