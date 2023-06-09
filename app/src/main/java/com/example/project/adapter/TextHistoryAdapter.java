package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.sqlite.TextHistoryDao;
import com.example.project.sqlite.pojo.TextHistoryBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TextHistoryAdapter extends BaseAdapter {


    Context context;
    List<TextHistoryBean> texts = new ArrayList<>();


    public TextHistoryAdapter(Context context, List<TextHistoryBean> texts, TextHistoryFresh textFresh) {
        super();
        this.texts = texts;
        this.context = context;
        this.textFresh = textFresh;
    }


    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public Object getItem(int position) {

        return texts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void init(ViewHolder holder, View view, int position) {


        holder.title = view.findViewById(R.id.item_tx_his_title);
        holder.time = view.findViewById(R.id.item_tx_his_date);
        holder.rate = view.findViewById(R.id.item_tx_his_rate);
        holder.delete = view.findViewById(R.id.item_tx_his_delete);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });


    }

    public void remove(int position) {
        textFresh.sendDeletePosition(position);
    }


    public void setTexts(List<TextHistoryBean> texts) {
        this.texts = texts;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_text_histroy, parent, false);
            init(holder, convertView, position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //填充数据
        if (texts != null && !texts.isEmpty()) {
            setText(holder, position);
        }

        return convertView;
    }

    private void setText(ViewHolder holder, int position) {
        holder.title.setText(texts.get(position).getTitle());
        holder.time.setText(texts.get(position).getTime());
        holder.rate.setText(String.valueOf(texts.get(position).getRate()));

    }


    //声明刷新接口
    public interface TextHistoryFresh {
        void sendDeletePosition(Integer position);

    }


    //接口对象
    private TextHistoryFresh textFresh;


    public void setTextFresh(TextHistoryFresh textFresh) {
        this.textFresh = textFresh;
    }

    private static class ViewHolder {
        TextView title, time, rate;
        ImageView delete;

    }

}
