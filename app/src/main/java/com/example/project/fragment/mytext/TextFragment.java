package com.example.project.fragment.mytext;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.project.util.DialogUtils;
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
import okhttp3.Request;
import okhttp3.Response;


public class TextFragment extends Fragment implements TextItemAdapter.TextFresh {

//    SwipeRefreshLayout refreshLayout;
    ListView listView;
    List<Text> texts ;
    TextItemAdapter adapter;
//    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        init(view);
        setInitData();
        return view;
    }


    private void setInitData() {
        List<Text> data = getData();
        LoggerUtils.e("TextFragment", "设置数据");
        adapter = new TextItemAdapter(getContext());
        adapter.setData(data);
        adapter.setRefreshData(this);
        listView.setAdapter(adapter);// 将适配器装入ListView对象

    }

    public void init(View view) {
        listView = view.findViewById(R.id.fra_text_listview);
//        refreshLayout = view.findViewById(R.id.fra_text_refressh);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData(); //刷新数据
//            }
//        });
    }

//    private void refreshData() {
//        //刷新数据，运行在默认线程中
//
//            //从本地或网络中获取最新数据
//            //...
//            //更新适配器中的数据
//        adapter.setText(texts);
//        adapter.notifyDataSetChanged();
//            //停止刷新动画
////        textItemAdapter.setRefreshing(false);
//
//    }

    public List<Text> getData() {
        LoggerUtils.e("TextFragment", "拿数据数据开始");
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
                                Type type = new TypeToken<List<Text>>() {
                                }.getType();//指定合适的 Type 类型
                                texts = gson.fromJson(info, type);
                                LoggerUtils.i("+TextFragment", texts.toString());
                                adapter.setData(texts);
                                adapter.notifyDataSetChanged();
                                adapter.setRefreshData(TextFragment.this);

                            }
                        });
                    }
                }

        );

        LoggerUtils.e("TextFragment", "拿数据数据结束");
        return texts;
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