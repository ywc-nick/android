package com.example.project.fragment.mytext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.ArticleActivity;
import com.example.project.adapter.TextItemAdapter;
import com.example.project.pojo.Like;
import com.example.project.pojo.Text;
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
import okhttp3.Response;


public class LikeFragment extends Fragment implements TextItemAdapter.TextFresh {


    SwipeRefreshLayout refreshLayout;
    ListView listView;
    List<Text> texts = new ArrayList<>();
    TextItemAdapter adapter;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!texts.isEmpty()) {
                        adapter.setData(texts);
                        adapter.notifyDataSetChanged();
                        adapter.setRefreshData(LikeFragment.this);
                    }
                    break;
                case 1:
                    // 停止下拉刷新
                    refreshLayout.setRefreshing(false);

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        init(view);
        setInitData();
        return view;
    }

    private void setInitData() {
        List<Text> data = getData();
        adapter = new TextItemAdapter(getContext());
        adapter.setRefreshData(this);
        adapter.setData(data);
        adapter.setRefreshData(this);
        listView.setAdapter(adapter);// 将适配器装入ListView对象

    }

    public void init(View view) {
        listView = view.findViewById(R.id.fra_like_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!texts.isEmpty()){
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    intent.putExtra("text",texts.get(position));
                    LoggerUtils.i("TextFragment",position+"");
                    getActivity().startActivity(intent);
                }
            }
        });
        refreshLayout = view.findViewById(R.id.fra_like_refressh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setInitData(); //刷新数据
                Message successMessage = mHandler.obtainMessage(1);
                mHandler.sendMessage(successMessage);
            }
        });
    }

    public List<Text> getData() { ;
        Map map = SharedPreferencesUtils.getSharePreferences(getContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);

        Gson gson = new Gson();
        String url = OkHttpUtil.baseUrl + "/like/custer/" + id;

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
                                Type type = new TypeToken<List<Like>>() {
                                }.getType();//指定合适的 Type 类型
                                if (info != null && !("".equals(info))) {
                                    List<Like> likeList = gson.fromJson(info, type);
                                    ArrayList<Text> temp = new ArrayList<>();
                                    for (Like like : likeList) {
                                        temp.add(like.getText());
                                    }
                                    texts = temp;
                                    LoggerUtils.i("+TextFragment", LikeFragment.this.texts.toString());
                                    Message successMessage = mHandler.obtainMessage(0);
                                    mHandler.sendMessage(successMessage);
                                }
                            }
                        });
                    }
                }

        );

        return texts;
    }

    @Override
    public void sendDeleteTid(int tid) {
        Map sharePreferences = SharedPreferencesUtils.getSharePreferences(getContext());
        Integer cid = (Integer) sharePreferences.get(SharedPreferencesUtils.ID);
        String url = OkHttpUtil.baseUrl + "/text/delete/" + tid + "/" + cid;

        OkHttpUtil.delete(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(data)) {
                            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在 Fragment 销毁时，移除所有消息，防止 Activity 调用已经销毁的 Fragment 的 Handler ，防止内存泄漏
        mHandler.removeCallbacksAndMessages(null);
    }

}