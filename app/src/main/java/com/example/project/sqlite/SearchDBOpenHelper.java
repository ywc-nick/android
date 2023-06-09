package com.example.project.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.util.LoggerUtils;

public class SearchDBOpenHelper {
//
//    private static final String DATABASE_NAME = "search_history.db";
//    private static final int DATABASE_VERSION = 1;
//
//    public SearchDBOpenHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
////        db.execSQL("CREATE TABLE search_history (_id INTEGER PRIMARY KEY AUTOINCREMENT, query TEXT unique, timestamp INTEGER)");
//          db.execSQL("CREATE TABLE search_history (_id INTEGER PRIMARY KEY AUTOINCREMENT,  TEXT unique, timestamp INTEGER)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS search_history");
//        onCreate(db);
//    }


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
            db.insertOrThrow("search_history", null, values);
        } catch (Exception e) {
            LoggerUtils.i("历史记录数据重复");
        } finally {

        }

    }

    public void delete(long id) {

        String[] whereArgs = {String.valueOf(id)};
        db.delete("search_history", "_id=?", whereArgs);


    }

    public void clear() {

        db.delete("search_history", null, null);


    }
}