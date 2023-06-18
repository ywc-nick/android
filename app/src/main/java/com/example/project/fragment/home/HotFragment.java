package com.example.project.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


public class HotFragment extends Fragment implements TextAdapter.ItemClickInterface{


    Boolean option = false;
    RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;//上拉刷新替换

    TextAdapter adapter;
    private List<Text> dataList =new ArrayList<>() ;
    private int currentPage = 1;

     Handler mHandler =new Handler(Looper.getMainLooper()) {
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what) {
                 case 1:
                     if (!dataList.isEmpty()){
                         adapter.updateData(dataList);
                         LoggerUtils.i("+TextFragment", dataList.toString());
                     }else {
                         Toast.makeText(getContext(), "已经到底了！！！", Toast.LENGTH_SHORT).show();
                     }

                     break;
                 case 2:
                     if (!dataList.isEmpty()){
                         adapter.setTexts(dataList);//替换数据
                         adapter.notifyDataSetChanged();
                     }else {
                         Toast.makeText(getContext(), "全部数据了！！！", Toast.LENGTH_SHORT).show();
                     }

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
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        init(view);

        return view;
    }



    public void init(View view){
        refreshLayout = view.findViewById(R.id.fra_hot_refresh_layout);
        refresh();//下拉替换
        recyclerView = view.findViewById(R.id.fra_hot_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//new LinearLayoutManager(getContext()// 创建一个垂直方向的布局管理器
        adapter = new TextAdapter(getActivity(),dataList,this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();//获取布局管理器
                    int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();//最后一项
                    int itemCount = linearLayoutManager.getItemCount();//item总数
                    if (lastVisiblePosition == (itemCount - 1)) {
                        option = true;
                        requestData();
                    }
                }
            }
        });
        requestData();
    }

    public void  refresh(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                option = false;
                requestData();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    public void requestData() {
        Gson gson = new Gson();
        String url = OkHttpUtil.baseUrl+"/text/like/" + currentPage;
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
                                    Message successMessage;
                                    if (option){
                                        successMessage = mHandler.obtainMessage(1);
                                    }else {
                                        successMessage = mHandler.obtainMessage(2);
                                    }
                                    currentPage++;
//                                    successMessage.obj = dataList;
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
        // 在 Fragment 销毁时，移除所有消息，防止 Activity 调用已经销毁的 Fragment 的 Handler ，防止内存泄漏
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onItemClick(int position, Text text) {
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra("text",text);
        getActivity().startActivity(intent);
    }
}