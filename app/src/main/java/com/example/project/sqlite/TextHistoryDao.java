package com.example.project.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.sqlite.pojo.TextHistoryBean;
import com.example.project.util.LoggerUtils;
import com.example.project.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TextHistoryDao {

    public static final String TABLE_NAME = "t_text_history";

    //列名

    public static final String TEXT_ID = "t_id";
    public static final String ID = "id";
    public static final String RATE = "rate";
    public static final String TITLE = "title";
    public static final String TIME = "time";

    public static final String CREATE_TABLE_MEMO = "create table " + TABLE_NAME +
            " (" + ID + " integer primary key AUTOINCREMENT, "
            + TEXT_ID + " integer, "
            + RATE + " integer, "
            + TITLE + " text unique,"
            + TIME + " TIMESTAMP OT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')) );";

    public static final String DROP_TABLE_MEMO = "drop table if exists " + TABLE_NAME + ";";


    DBManager dbManager;
    SQLiteDatabase db;


    // 初始化打开数据库
    public TextHistoryDao(Context context) {
        dbManager = new DBManager(context);
        db = dbManager.getWritableDatabase();
    }

    //插入
    public Long insert(TextHistoryBean textHistory){
        ContentValues cv = new ContentValues();
        cv.put(TEXT_ID, textHistory.getTid());
        cv.put(TITLE, textHistory.getTitle());
        cv.put(TEXT_ID, textHistory.getTid());
        cv.put(RATE, textHistory.getRate());
        Long l = null;
        try {
            //使用 onConflict 关键字的 insert() 方法可以在插入发生冲突时，直接执行更新操作，而不是抛出异常
             l= db.insertWithOnConflict(TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
        }catch (Exception e){
            LoggerUtils.e("已插入");
        }
        return l;
    }

    //查询
    public List<TextHistoryBean> queryAll() {
        String[] columns = new String[]{
                ID,TEXT_ID,TITLE,TIME,RATE
        };
        Cursor query = db.query(false, TABLE_NAME, columns, null, null, null, null, null, null);
        List<TextHistoryBean> list = new ArrayList<>();
        while (query.moveToNext()) {
            int id = query.getInt(query.getColumnIndexOrThrow(ID));
            int tid = query.getInt(query.getColumnIndexOrThrow(TEXT_ID));
            Integer rate = query.getInt(query.getColumnIndexOrThrow(RATE));
            String title = query.getString(query.getColumnIndexOrThrow(TITLE));
            String time = TimeUtils.timestampToString(query.getType(query.getColumnIndexOrThrow(TIME)));
            list.add(new TextHistoryBean(id,title, time ,rate,tid));
        }
        return list;
    }




    //4.delete
    public int delete(int id) {
        //1.where子句
        String whereClause = ID + "=?";
        //2.whereArgs
        String[] whereArgs = {String.valueOf(id)};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    //4.delete
    public void deleteList(List<Integer> list) {

        String whereClause = ID + "=?";

        String[] whereArgs;
        for (Integer id:list){
            whereArgs = new String[]{String.valueOf(id)};
            db.delete(TABLE_NAME, whereClause, whereArgs);
        }
    }

    //4.delete
    public void deleteAll() {

        db.delete(TABLE_NAME, null, null);
        //每张表都有一个名为sqlite_sequence的系统表，用于记录每张表的自增ID的当前值。
        // 然后使用SQL语句来重置当前的SQLite对自增ID的记录
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "'");

    }


}

