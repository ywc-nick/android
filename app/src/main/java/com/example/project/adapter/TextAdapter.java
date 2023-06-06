package com.example.project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.pojo.Text;
import com.example.project.util.ImageUtils;
import com.example.project.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    Context context;
    List<Text> texts = new ArrayList<>();

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public TextAdapter() {
    }

    public TextAdapter(Context context, List<Text> texts) {
        this.context = context;
        this.texts = texts;
    }

    //获取视图
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_item, parent, false);
        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        if (!texts.isEmpty() && texts != null) {
            Text text = texts.get(position);
            holder.setData(text);
        }

    }


    @Override
    public int getItemCount() {
        if (texts.isEmpty() || texts == null) {
            return 1;
        }
        return texts.size();

    }

    public void updateData(List<Text> newTexts) {

        int oldDataSize = getItemCount();
        int newDataSize = newTexts.size();

        texts.addAll(newTexts);
        notifyItemRangeInserted(oldDataSize, newDataSize);//Todo
    }


    public class TextViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title, content, nickname, like, collect, date;


        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.text_item_image);
            title = itemView.findViewById(R.id.text_item_title);
            nickname = itemView.findViewById(R.id.text_item_nickname);
            content = itemView.findViewById(R.id.text_item_text);
            like = itemView.findViewById(R.id.text_item_like);
            collect = itemView.findViewById(R.id.text_item_collect);
            date = itemView.findViewById(R.id.text_item_date);
        }

        public void setData(Text text) {
            image.setImageBitmap(ImageUtils.getRoundedCornerBitmap(text.getCuster().getImagebytes(), ImageUtils.smallimage));
//            image.setImageBitmap(ImageUtils.String2Bitmap(text.getCuster().getImagebytes()));
            title.setText(text.getTheme());
            nickname.setText(text.getCuster().getVir_name());
            content.setText(text.getArticle());
            like.setText(String.valueOf(text.getKnums()));
            collect.setText(String.valueOf(text.getKnums()));
            date.setText(TimeUtils.TimestampToDateString(text.getC_date()));
        }
    }
}
