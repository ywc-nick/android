package com.example.project.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    ItemClickInterface itemClickInterface;


    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public TextAdapter() {
    }

    public TextAdapter(Context context, List<Text> texts, ItemClickInterface itemClickInterface) {
        this.context = context;
        this.texts = texts;
        this.itemClickInterface = itemClickInterface;
    }

    //获取视图
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_item, parent, false);

        return new TextViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!texts.isEmpty()) {
            Text text = texts.get(position);
            holder.setData(text);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!texts.isEmpty() && itemClickInterface != null) {
                    itemClickInterface.onItemClick(position, texts.get(position));
                }
            }
        });
//        Toast.makeText(context.getApplicationContext(), text.getTheme() + " | " + position, Toast.LENGTH_SHORT).show();
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
        notifyItemRangeInserted(oldDataSize, newDataSize);
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


    //传送数据
    public interface ItemClickInterface {
        void onItemClick(int position, Text text);
    }
}
