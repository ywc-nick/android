package com.example.project.fragment.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.MyFragment;
import com.example.project.pojo.Kind;
import com.example.project.pojo.Text;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RecommendFragment extends Fragment implements KindFragment.KindInterface{


    View view;//容器视图
    TabLayout tableLayout;


    //碎片管理器
    FragmentManager fm;
    FragmentTransaction ft;
    int posit;//碎片和tablayout的位置

    Handler mHandler =new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    if (!titles.isEmpty()) {
                        setData();
                        initTabLayout();
                        LoggerUtils.i("+RecommendFragment", titles.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public static List<Kind> titles = new ArrayList<>();
    List<Fragment> fragmentsList;//碎片集合，不用每次都new，可替换旧的

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend, container, false);
        tableLayout = view.findViewById(R.id.fra_recommend_tablayout);
        requestData();
        return view;
    }

    public void setData() {

        fragmentsList = new ArrayList<>();

        for (int i = 0; i < titles.size(); i++) {
            KindFragment fragment = new KindFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("kind",titles.get(i).getKid());
            fragment.setArguments(bundle);
            fragmentsList.add(fragment);
        }


        List<Handler> handlerList = new ArrayList<>();
//        for (Fragment fragment : fragmentsList) {
//            Handler handler = fragment.getView().getHandler();
//            handlerList.add(handler);
//        }



        replaceFragment(fragmentsList.get(0));

    }

    /**
     * 测试数据
     */


    public void initTabLayout() {
        LoggerUtils.i(titles.size() + "");
        for (int i = 0; i < titles.size(); i++) {
            tableLayout.addTab(tableLayout.newTab().setText(titles.get(i).getContent()).setTag(i));//tag设为下标
            LoggerUtils.i(titles.get(i) + " " + i);
        }
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                posit = (Integer) tab.getTag();
                replaceFragment(fragmentsList.get(posit));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    private void requestData() {
        Gson gson = new Gson();
        String url = OkHttpUtil.baseUrl+"/other/kind";
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
                        Type type = new TypeToken<List<Kind>>() {}.getType();//指定合适的 Type 类型
                        try {

                            Kind all = new Kind(0, "全部");//加上全部类型
                            titles= gson.fromJson(info, type);
                            titles.add(0,all);
                            Message successMessage = mHandler.obtainMessage(1);
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


    public void replaceFragment(Fragment fragment) {
        //1. 获取碎片管理器
        fm = getChildFragmentManager();
        //2. 开启碎片事务
        ft = fm.beginTransaction();
        //3. 替换碎片
        ft.replace(R.id.fra_recommend_blank, fragment);
        LoggerUtils.i("碎片已替换");
        //4. 提交事务
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void sendToParent(int kid, List<Text> textsData) {

    }
}

