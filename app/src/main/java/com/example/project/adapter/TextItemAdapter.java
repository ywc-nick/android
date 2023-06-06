package com.example.project.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.R;
import com.example.project.pojo.Text;
import com.example.project.util.DialogUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextItemAdapter extends BaseAdapter {


    Context context;
    List<Text> texts = new ArrayList<>();
    //删除元素tid集合
    //接口对象
    private TextFresh textFresh;
    public TextItemAdapter(Context context) {
        super();
        this.context = context;
    }


    @Override
    public int getCount() {
        if (texts==null||texts.isEmpty()) {
            return 1;
        }
//        LoggerUtils.i(",,,,,",texts.toString());
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        if (!texts.isEmpty()&&texts!=null) {
            return texts.get(position);
        }
        return new Text();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void init(ViewHolder holder, View view, int position) {

        holder.title = view.findViewById(R.id.mytext_item_title);
        holder.text = view.findViewById(R.id.mytext_item_text);
        holder.like = view.findViewById(R.id.mytext_item_like);
        holder.collect = view.findViewById(R.id.mytext_item_collect);
        holder.delete = view.findViewById(R.id.mytext_item_delete);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

    }

    public void remove(int position) {
        int id = texts.get(position).getTid();
        DialogUtils.showConfirmDialogDelete(context, "确定删除此文章吗", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                texts.remove(position);
                notifyDataSetChanged();
                textFresh.sendDeleteTid(id);
            }
        });

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.mytext_item, parent, false);
//            convertView = View.inflate(context, R.layout.mytext_item,parent);

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
        holder.text.setText(texts.get(position).getArticle());
        holder.title.setText(texts.get(position).getTheme());
        holder.like.setText(String.valueOf(texts.get(position).getKnums()));
        holder.collect.setText(String.valueOf(texts.get(position).getCnums()));
    }

    public void setData(List<Text> texts) {
        this.texts = texts;
    }


    //声明刷新总价接口
    public interface TextFresh {
        void sendDeleteTid(int tid);
    }



    public void setRefreshData(TextFresh textFresh) {
        this.textFresh = textFresh;
    }

    private static class ViewHolder {
        ImageView delete;
        TextView title, text, like, collect;
    }

}
