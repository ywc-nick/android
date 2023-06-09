package com.example.project.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sqlite.db";


    private Context context;

    public DBManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        Log.e("---------DBManager-------", String.valueOf(context));
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(TextHistory.CREATE_TABLE_USER);
        db.execSQL("CREATE TABLE search_history (_id INTEGER PRIMARY KEY AUTOINCREMENT, query TEXT unique, timestamp INTEGER)");
        db.execSQL(TextHistoryDao.CREATE_TABLE_MEMO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(UserUtil.DROP_TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS search_history");
        db.execSQL(TextHistoryDao.DROP_TABLE_MEMO);
        onCreate(db);
    }
}
