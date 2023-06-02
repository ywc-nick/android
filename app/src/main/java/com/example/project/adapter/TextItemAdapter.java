package com.example.project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.pojo.Text;
import com.example.project.util.LoggerUtils;


import java.util.List;

public class TextItemAdapter extends BaseAdapter {

    CheckBox checkBox;
    TextView title,text,like,collect;

    Context context;
    List<Text> texts;

    public TextItemAdapter(Context context, List<Text> texts){
        super();
        this.context = context;
        this.texts = texts;
    }

    @Override
    public int getCount() {
        if (texts==null){
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
            init(convertView);
            convertView = View.inflate(context, R.layout.mytext_item,parent);
        }
        setData(texts.get(position));
        return convertView;
    }

    private void setData(Text text1) {
        text.setText(text1.getArticle());
        title.setText(text1.getTheme());
        like.setText(text1.getKnums());
        collect.setText(text1.getCnums());
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
