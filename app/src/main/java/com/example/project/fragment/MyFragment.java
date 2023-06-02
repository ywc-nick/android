package com.example.project.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.activity.MyTextActivity;
import com.example.project.pojo.Collect;
import com.example.project.pojo.Like;
import com.example.project.pojo.Custer;
import com.example.project.pojo.Text;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MyFragment extends Fragment {


    ImageView image;//头像
    TextView nickname,likeView,collectView,textView;//昵称

    private String url = "http://10.0.2.2:8080/qianxun/app";

    Gson gson = new Gson();

    public static final String opt = "option";//Like2CollectActivity碎片指定

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataFill();//填充数据
    }

    public void init( View view){
        image = view.findViewById(R.id.fra_my_image);
        nickname = view.findViewById(R.id.fra_my_nickname);
        textView = view.findViewById(R.id.fra_my_text);
        collectView = view.findViewById(R.id.fra_my_collect);
        likeView = view.findViewById(R.id.fra_my_like);

        collectView.setOnClickListener(listener);
        likeView.setOnClickListener(listener);
        textView.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.fra_my_like:
                    intent = new Intent(getActivity(), MyTextActivity.class);
                    intent.putExtra(opt,0);
                    startActivity(intent);
                    break;
                case R.id.fra_my_text:
                    intent = new Intent(getActivity(), MyTextActivity.class);
                    intent.putExtra(opt,1);
                    startActivity(intent);
                    break;
                case R.id.fra_my_collect:
                    intent = new Intent(getActivity(), MyTextActivity.class);
                    intent.putExtra(opt,2);
                    startActivity(intent);
                    break;


            }
        }
    };

    /**
     * 填充数据
     */
    public void dataFill(){
        //TODO 从共享首选项中拿取id

        Map map = SharedPreferencesUtils.getSharePreferences(getContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);

        if (id == 0){
            return;
        }
        url = "http://10.0.2.2:8080/qianxun/app1/custer/" + id;
        OkHttpUtil.get(url, new Callback(){
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
//                LoggerUtils.i("get data success",info);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Custer custer = gson.fromJson(info,Custer.class);
                        byte[] imagebytes = Base64.decode(custer.getImagebytes(),Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
                        textView.setText(custer.getNums().toString());
                        image.setImageBitmap(bitmap);
                        nickname.setText(custer.getVir_name());
                    }
                });
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }
        });

        url = "http://10.0.2.2:8080/qianxun/app1/other/" + id;
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string();

              getActivity().runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      Type type = new TypeToken<Map<String, List>>(){}.getType();//指定合适的 Type 类型
                      Map map = gson.fromJson(info, type);
                      LoggerUtils.i(map.toString());
                      List likesnum = (List) map.get("likesnum");
                      List collectsnum = (List) map.get("collectsnum");
                      collectView.setText(String.valueOf(Math.round((Double) collectsnum.get(0))));
                      likeView.setText(String.valueOf(Math.round((Double) likesnum.get(0))));

                  }
              });
            }
        });


    }


}