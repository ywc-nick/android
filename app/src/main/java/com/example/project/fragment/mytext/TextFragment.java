package com.example.project.fragment.mytext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.adapter.TextItemAdapter;
import com.example.project.pojo.Collect;
import com.example.project.pojo.Like;
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
import okhttp3.Request;
import okhttp3.Response;


public class TextFragment extends Fragment implements TextItemAdapter.TextFresh {


    ListView listView;
    List<Text> texts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        getData();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        init(view);
        getData();
        setData();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setData();
    }

    private void setData() {
        // 将数据装入适配器 data即数据
        TextItemAdapter textItemAdapter = new TextItemAdapter(getContext(), texts);
        textItemAdapter.setRefreshData(this);
        listView.setAdapter(textItemAdapter);// 将适配器装入ListView对象
        listView.deferNotifyDataSetChanged();//listview同步数据

    }

    public void init(View view) {
        listView = view.findViewById(R.id.fra_text_listview);
    }

    public void getData() {

        Map map = SharedPreferencesUtils.getSharePreferences(getContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);

        Gson gson = new Gson();
        String url = "http://10.0.2.2:8080/qianxun/app1/text/custer/" + id;

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
                        Type type = new TypeToken<List<Text>>(){}.getType();//指定合适的 Type 类型
                        texts = gson.fromJson(info, type);
                        LoggerUtils.i("TextFragment",texts.toString());

                    }
                });
            }
        });
    }

    @Override
    public void refreshPrice(List<Text> texts) {
        this.texts = texts;
    }


    //        Request request = new Request.Builder().url(url).get().build();
//
//        String info = OkHttpUtil.execute(request);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Type type = new TypeToken<List<Text>>() {
//                }.getType();//指定合适的 Type 类型
//                texts = gson.fromJson(info, type);
//                LoggerUtils.i("TextFragment", texts.toString());
//            }
//        });
}