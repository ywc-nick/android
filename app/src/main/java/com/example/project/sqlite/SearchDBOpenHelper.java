package com.example.project.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.util.LoggerUtils;

public class SearchDBOpenHelper {


    DBManager dbManager;
    SQLiteDatabase db;


    // 初始化打开数据库
    public SearchDBOpenHelper(Context context) {
        dbManager = new DBManager(context);
        db = dbManager.getWritableDatabase();
    }

    public Cursor getListCursor() {
        String[] columns = {"_id", "query"};
        String orderBy = "timestamp DESC";
        return db.query("search_history", columns, null, null, null, null, orderBy);
    }

    public void insert(String query) {

        ContentValues values = new ContentValues();
        values.put("query", query);
        values.put("timestamp", System.currentTimeMillis());
        try {
            //使用 onConflict 关键字的 insert() 方法可以在插入发生冲突时，直接执行更新操作，而不是抛出异常
//            db.insertWithOnConflict(TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
            db.insertWithOnConflict("search_history", null, values,SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            LoggerUtils.i("历史记录数据重复");
        }

    }

    public void delete(long id) {
        String[] whereArgs = {String.valueOf(id)};
        db.delete("search_history", "_id=?", whereArgs);
    }

    public void clear() {
        db.delete("search_history", null, null);
//        db.execSQL("DELETE FROM sqlite_sequence WHERE name = search_history");
    }
}