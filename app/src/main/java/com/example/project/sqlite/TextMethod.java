package com.example.project.sqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.pojo.Text;

public class TextMethod {
    SQLiteDatabase db;

    public TextMethod(SQLiteDatabase db) {

        this.db = db;

    }



    public boolean save(Object objects) {

        try {

            Text text = (Text) objects;

            ContentValues values = new ContentValues();

            values.put("date",String.valueOf(text.getC_date()));

            values.put("title", text.getTheme());

            values.put("content", text.getArticle());

            long index = db.insert("t_message", null, values);

            if (index > 0)

                return true;

            else {

                return false;

            }

        } catch (Exception e) {

            return false;

        }

    }
}
