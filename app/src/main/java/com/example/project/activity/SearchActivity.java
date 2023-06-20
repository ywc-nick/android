package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

/**
 * @Author zhangxiaolong
 */
public class SearchActivity extends AppCompatActivity {

    private EditText mSearchBox;
    private ListView mSearchHistory;
    private SearchHistoryAdapter mAdapter;
    private SearchDBOpenHelper mDbHelper;

    private TextView search_button;

    private ImageView iv_searchback;

    ImageButton clear_history;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        setDb();


    }
    public void setDb(){
        // 创建数据库帮助类实例
        mDbHelper = new SearchDBOpenHelper(this);
        // 创建历史记录适配器，并将其设置为搜索历史记录列表的适配器
        Cursor cursor = mDbHelper.getListCursor();
        mAdapter = new SearchHistoryAdapter(this, cursor);
        mSearchHistory.setAdapter(mAdapter);
        // 设置历史记录列表的点击监听器，用于重新搜索历史记录项
        mSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow("query"));
//                Toast.makeText(getApplicationContext(), displayName, Toast.LENGTH_SHORT).show();
                mSearchBox.setText(displayName);
                mSearchBox.setSelection(displayName.length());
                //跳转
                Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("result",displayName);
                startActivity(intent);
            }
        });
    }




    private void init() {
        iv_searchback = findViewById(R.id.iv_searchback_image);
        search_button = findViewById(R.id.search_button);
        // 获取搜索框和历史记录列表
        clear_history = findViewById(R.id.act_search_clear_all);
        mSearchBox = findViewById(R.id.search_box);
        mSearchHistory = findViewById(R.id.search_history);
        iv_searchback = findViewById(R.id.iv_searchback_image);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = mSearchBox.getText().toString();
                if (!"".equals(res)&&res !=null){
                    saveHistory();
                    //跳转
                    Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                    intent.putExtra("result",res);
                    startActivity(intent);
                }

            }
        });

        //自动启动键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    imm.showSoftInput(mSearchBox, InputMethodManager.SHOW_IMPLICIT);

                }else {
                    imm.hideSoftInputFromWindow(mSearchBox.getWindowToken(), 0);
                }
            }
        });
        mSearchBox.requestFocus();//获得光标


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

        iv_searchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void saveHistory(){
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
    }


}