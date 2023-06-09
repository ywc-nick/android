package com.example.project.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.ArticleActivity;
import com.example.project.activity.InfoActivity;
import com.example.project.activity.LoginActivity;
import com.example.project.activity.MyTextActivity;
import com.example.project.activity.RegisterActivity;
import com.example.project.activity.SettingActivity;
import com.example.project.adapter.TextHistoryAdapter;
import com.example.project.pojo.Collect;
import com.example.project.pojo.Like;
import com.example.project.pojo.Custer;
import com.example.project.pojo.Text;
import com.example.project.sqlite.TextHistoryDao;
import com.example.project.sqlite.pojo.TextHistoryBean;
import com.example.project.util.DialogUtils;
import com.example.project.util.ImageUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MyFragment extends Fragment implements TextHistoryAdapter.TextHistoryFresh {


    ImageView image;//头像
    TextView nickname, likeView, collectView, textView;//昵称
    FrameLayout info;

    ListView listView;
    ImageView allDelete, setting;
    List<TextHistoryBean> textHistorys;
    TextHistoryAdapter textHistoryAdapter;
    TextHistoryDao textHistoryDao;

    Gson gson = new Gson();
    Custer custer;
    Map maps;
    Text text;

    public static final String opt = "option";//Like2CollectActivity碎片指定

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    textView.setText(custer.getNums().toString());
//                        image.setImageBitmap(ImageUtils.getCircularBitmap(bitmap));
                    if (custer.getImagebytes()!=null){
                        image.setImageBitmap(ImageUtils.getRoundedCornerBitmap(custer.getImagebytes(), ImageUtils.bigimage));
                    }

                    nickname.setText(custer.getVir_name());
                    break;
                case 2:
                    List likesnum = (List) maps.get("likesnum");
                    List collectsnum = (List) maps.get("collectsnum");
                    collectView.setText(String.valueOf(Math.round((Double) collectsnum.get(0))));
                    likeView.setText(String.valueOf(Math.round((Double) likesnum.get(0))));
                    break;
                case 3:
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    intent.putExtra("text",text);
                    getActivity().startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        init(view);
        initHistory(view);
        dataFill();//填充数据
        return view;
    }

    public void init(View view) {
        image = view.findViewById(R.id.fra_my_image);
        nickname = view.findViewById(R.id.fra_my_nickname);
        textView = view.findViewById(R.id.fra_my_text);
        collectView = view.findViewById(R.id.fra_my_collect);
        likeView = view.findViewById(R.id.fra_my_like);
        info = view.findViewById(R.id.fra_my_info);
        setting = view.findViewById(R.id.fra_my_setting);

        setting.setOnClickListener(listener);
        collectView.setOnClickListener(listener);
        likeView.setOnClickListener(listener);
        textView.setOnClickListener(listener);
        info.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        Intent intent;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.fra_my_like:
                    intent = new Intent(getActivity(), MyTextActivity.class);
                    intent.putExtra(opt, 0);
                    startActivity(intent);
                    break;
                case R.id.fra_my_text:
                    intent = new Intent(getActivity(), MyTextActivity.class);
                    intent.putExtra(opt, 1);
                    startActivity(intent);
                    break;
                case R.id.fra_my_collect:
                    intent = new Intent(getActivity(), MyTextActivity.class);
                    intent.putExtra(opt, 2);
                    startActivity(intent);
                    break;
                case R.id.fra_my_info:
                    startActivity(new Intent(getActivity(), InfoActivity.class));
                    break;
                case R.id.fra_my_setting:
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    break;

            }
        }
    };

    //数据库查询
    public void query() {
        textHistoryDao = new TextHistoryDao(getContext());
        textHistorys = textHistoryDao.queryAll();
        textHistoryAdapter.setTexts(textHistorys);
        textHistoryAdapter.setTextFresh(this);

    }


    private void initHistory(View view) {

        listView = view.findViewById(R.id.fra_my_list);

        textHistoryAdapter = new TextHistoryAdapter(getContext(), textHistorys, this);
        query();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!textHistorys.isEmpty()) {
                    getInformation( textHistorys.get(position).getTid());
                }
            }
        });
        allDelete = view.findViewById(R.id.fra_my_delete);
        allDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showConfirmDialogDelete(getContext(), "确定全部删除吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textHistoryDao.deleteAll();
                        textHistorys.clear();
                        TextHistoryAdapter temp = new TextHistoryAdapter(getContext(), textHistorys, MyFragment.this);
                        temp.setTextFresh(MyFragment.this);
                        listView.setAdapter(temp);
                        listView.deferNotifyDataSetChanged();//数据同步
                    }
                });
//                textHistoryAdapter.setTexts(texts);
//                listView.deferNotifyDataSetChanged();//数据同步
//                textHistoryAdapter.setTextFresh(MyFragment.this);
            }
        });
        listView.setAdapter(textHistoryAdapter);


    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //ToDo
//        textHistoryDao = new TextHistoryDao(getContext());
//        textHistoryDao.insert(new TextHistoryBean(1,"12fgddfg31",80));
//        textHistoryDao.insert(new TextHistoryBean(2,"1fadf1",100));
//        textHistoryDao.insert(new TextHistoryBean(3,"1fafffnndfdf1",0));
//        textHistoryDao.insert(new TextHistoryBean(5,"1ffggouaggdddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(4,"1ffggouoaggdddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(6,"1ffggauououggdddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(7,"1ffggaggdddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(8,"1fndneradddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(9,"1faderrqddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(10,"1fadddsdf1",0));
//        textHistoryDao.insert(new TextHistoryBean(11,"f",0));
//        textHistoryDao.insert(new TextHistoryBean(12,"fyury",0));
//        textHistoryDao.insert(new TextHistoryBean(13,"jjf",0));
//        textHistoryDao.insert(new TextHistoryBean(14,"1fadddf1",0));
//        textHistoryDao.insert(new TextHistoryBean(15,"1farerqvfbf1",100));
//
//    }


    /**
     * 填充数据
     */
    public void dataFill() {

        Map map = SharedPreferencesUtils.getSharePreferences(getContext());
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);

        if (id == 0) {
            return;
        }

        String url = OkHttpUtil.baseUrl + "/custer/" + id;
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
//                LoggerUtils.i("get data success",info);
                try {
                    custer = gson.fromJson(info, Custer.class);
                    Message message = mHandler.obtainMessage(1);
                    mHandler.sendMessage(message);
//                            byte[] imagebytes = Base64.decode(custer.getImagebytes(),Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
                } catch (Exception e) {
                    LoggerUtils.e("MyFragment数据为空", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }
        });

        url = OkHttpUtil.baseUrl + "/other/" + id;
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string();
                Type type = new TypeToken<Map<String, List>>() {
                }.getType();//指定合适的 Type 类型
                try {
                    maps = gson.fromJson(info, type);
                    Message message = mHandler.obtainMessage(2);
                    mHandler.sendMessage(message);
//                      LoggerUtils.i(map.toString());
                } catch (Exception e) {
                    LoggerUtils.e("MyFragment数据为空", e.getMessage());

                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        dataFill();
        query();
    }


    public void delete(Integer position) {
        Integer id = textHistorys.get(position).getId();
        textHistoryDao.delete(id);
        LoggerUtils.i("MyFragment", textHistorys.get(position).toString() + "删除成功");
        textHistorys = textHistoryDao.queryAll();
        TextHistoryAdapter temp = new TextHistoryAdapter(getContext(), textHistorys, this);
        textHistoryAdapter.setTextFresh(this);
        temp.notifyDataSetChanged();
        listView.setAdapter(temp);
        listView.deferNotifyDataSetChanged();//数据同步
    }
//    iAdapter = new ItemAdapter(MainActivity.this, data);
//        iAdapter.setRefreshPriceInterface(this);
//    // 将适配器装入ListView对象
//        mylist.setAdapter(iAdapter);
//        mylist.deferNotifyDataSetChanged();

    @Override
    public void sendDeletePosition(Integer position) {
        delete(position);
    }

    //显示查看文章的内容的方法
    private void getInformation(Integer tid) {

        if (tid == 0) {
            return;
        }
        String url = OkHttpUtil.baseUrl + "/text/" + tid;
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
//                LoggerUtils.i("get data success",info);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text = gson.fromJson(info, Text.class);
                        Message message = mHandler.obtainMessage(3);
                        mHandler.sendMessage(message);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }
        });
    }
}