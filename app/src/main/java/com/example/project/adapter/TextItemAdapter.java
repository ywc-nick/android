package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.pojo.Text;
import com.example.project.util.LoggerUtils;


import java.util.ArrayList;
import java.util.List;

public class TextItemAdapter extends BaseAdapter {

    CheckBox checkBox;
    TextView title,text,like,collect;

    Context context;
    List<Text> texts;

    public TextItemAdapter(Context context){
        super();
        this.context = context;

    }

    @Override
    public int getCount() {
        if (texts==null||texts.isEmpty()){
            return 0;
        }
        LoggerUtils.i(",,,,,",texts.toString());
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


    public void init(View view){

        title = view.findViewById(R.id.mytext_item_title);
        text = view.findViewById(R.id.mytext_item_text);
        like = view.findViewById(R.id.mytext_item_like);
        collect = view.findViewById(R.id.mytext_item_collect);
        checkBox = view.findViewById(R.id.mytext_item_check);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){

            convertView =  LayoutInflater.from(context).inflate( R.layout.mytext_item,parent,false);
//            convertView = View.inflate(context, R.layout.mytext_item,parent);
            init(convertView);
        }
        if (texts!=null&&!texts.isEmpty()){
            setText(texts.get(position));
        }

        return convertView;
    }

    private void setText(Text text1) {
        text.setText(text1.getArticle());
        title.setText(text1.getTheme());
        like.setText(String.valueOf(text1.getKnums()));
        collect.setText(String.valueOf(text1.getCnums()));
    }

    public void setData(List<Text> texts) {
        this.texts = texts;
    }


    //声明刷新总价接口
    public interface TextFresh {
        void refreshPrice(List<Text> texts);
    }

    //接口对象
    private TextFresh textFresh;

    public void setRefreshData(TextFresh textFresh) {
        this.textFresh = textFresh;
    }
}
