package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.adapter.SearchHistoryAdapter;
import com.example.project.adapter.TextItemAdapter;
import com.example.project.pojo.Text;
import com.example.project.sqlite.SearchDBOpenHelper;
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


public class SearchActivity extends AppCompatActivity {

    private EditText mSearchBox;
    private ListView mSearchHistory;
    private SearchHistoryAdapter mAdapter;
    private SearchDBOpenHelper mDbHelper;

    private TextView search_button;

    private ImageView iv_searchback;

    private ListView searchResultListView;

    private LinearLayout history_linera;

    Button clear_history;

    List<Text> list = new ArrayList<>();
    TextItemAdapter textItemAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();

        // 创建数据库帮助类实例
        mDbHelper = new SearchDBOpenHelper(this);

        // 创建历史记录适配器，并将其设置为搜索历史记录列表的适配器
        Cursor cursor = mDbHelper.getListCursor();
        mAdapter = new SearchHistoryAdapter(this, cursor);
        mSearchHistory.setAdapter(mAdapter);

        // 获取携带搜索结果的 Intent 实例
        Intent intent = getIntent();
        String result = intent.getStringExtra("serach");
        mSearchBox.setText(result);

        // 使用 ArrayAdapter 实现搜索结果在 ListView 中的展示

        textItemAdapter = new TextItemAdapter(getApplicationContext());
        searchResultListView.setAdapter(textItemAdapter);
        textItemAdapter.setData(list);
        searchResultListView.deferNotifyDataSetChanged();

    }

    //根据搜索框中输入的内容和数据库中的数据相匹配，拿数据
    private List<Text> showSearch() {
        String keyword = mSearchBox.getText().toString();
        String url = OkHttpUtil.baseUrl +"/text/search/"+ keyword;
        Gson gson = new Gson();

        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Type type = new TypeToken<List<Text>>() {}.getType();//指定合适的 Type 类型
                        LoggerUtils.i(string);
                        list = gson.fromJson(string, type);

                        textItemAdapter.setData(list);
//                        textItemAdapter.setRefreshData(this);
                        searchResultListView.deferNotifyDataSetChanged();
                    }
                });
            }
        });
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 更新历史记录列表
        Cursor cursor = mDbHelper.getListCursor();
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void init() {
        iv_searchback = findViewById(R.id.iv_searchback_image);
        search_button = findViewById(R.id.search_button);
        // 获取搜索框和历史记录列表
        mSearchBox = findViewById(R.id.search_box);
        mSearchHistory = findViewById(R.id.search_history);
        iv_searchback = findViewById(R.id.iv_searchback_image);
        searchResultListView = findViewById(R.id.search_result_listView);
        history_linera = findViewById(R.id.history_linera);
        clear_history = findViewById(R.id.clear_history_button);
        //自动启动键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                    imm.showSoftInput(mSearchBox, InputMethodManager.SHOW_IMPLICIT);
                    history_linera.setVisibility(View.VISIBLE);
                }else {
                    imm.hideSoftInputFromWindow(mSearchBox.getWindowToken(), 0);
                    history_linera.setVisibility(View.GONE);
                }
            }
        });
        mSearchBox.requestFocus();//获得光标
        mSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 在这里获取点击的内容
                String itemValue = (String) adapterView.getItemAtPosition(i);
                mSearchBox.setText(itemValue);
                Toast.makeText(getApplicationContext(), "Clicked Item: " + itemValue, Toast.LENGTH_SHORT).show();
            }
        });
        //setOnEditorActionListener

        // 设置历史记录列表的触摸监听器，用于删除历史记录
        mSearchHistory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    // 如果用户按下了列表，则暂停自动更新，直到用户松开手指
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        // 设置搜索框的文本变化监听器，用于搜索和展示历史记录
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = mSearchBox.getText().toString();
                if (!TextUtils.isEmpty(query)) {
                    // 将搜索框中的文本添加到历史记录中
//                    mDbHelper.insert(query);
//                    String query = cursor.getString(cursor.getColumnIndexOrThrow("query"));

                    // 启用清除历史记录
                    findViewById(R.id.clear_history_button).setEnabled(true);

                    // 显示与搜索关键字匹配的历史记录项
                    Cursor cursor = mDbHelper.getListCursor();
                    mAdapter.changeCursor(cursor);
                } else {
                    // 如果搜索框为空，则显示所有历史记录项
                    Cursor cursor = mDbHelper.getListCursor();
                    mAdapter.changeCursor(cursor);
                    // 禁用清除历史记录
                    clear_history.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 设置清除历史记录按钮的点击监听器，用于清除历史记录
        clear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHelper.clear();
                Cursor cursor = mDbHelper.getListCursor();
                mAdapter.changeCursor(cursor);
                clear_history.setEnabled(false);
            }
        });

        // 设置历史记录列表的点击监听器，用于重新搜索历史记录项
        mSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String query = cursor.getString(cursor.getColumnIndexOrThrow("query"));
                //mSearchBox.setText(query);
                mSearchBox.setSelection(query.length());
            }
        });

        // 设置搜索按钮的点击监听器，用于开始搜索
        mSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchBox.clearFocus();//失去光标
                history_linera.setVisibility(View.GONE);
                String query = mSearchBox.getText().toString();
//                Cursor cursor = mDbHelper.getListCursor();
                if (query != null && !("".equals(query))) {
                    // 将搜索框中的文本添加到历史记录中
                    try {
                        mDbHelper.insert(query);
                    } catch (Exception e) {
                        LoggerUtils.i("历史记录重复");
                    }
                }

                // 启用清除历史记录按钮
                clear_history.setEnabled(true);

                // 更新历史记录列表
                Cursor cursor = mDbHelper.getListCursor();
                mAdapter.changeCursor(cursor);
                showSearch();
            }
        });
        iv_searchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}