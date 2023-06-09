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

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> implements View.OnClickListener{

    Context context;
    List<Text> texts = new ArrayList<>();
    ItemClickInterface itemClickInterface;
    Text text = new Text();//传递的数据
    int post;//当前数据位置

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
        if (!texts.isEmpty()&&texts!=null){
            text = texts.get(position);//赋值
            post = position;
        }
        return super.getItemViewType(position);
    }



    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_item, parent, false);
        itemView.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (!texts.isEmpty()&&texts!=null&&itemClickInterface!=null){
            itemClickInterface.onItemClick(post,text);
        }
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
        void onItemClick(int position,Text text);
    }
}
