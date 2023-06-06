package com.example.project.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SharedPreferencesUtils {


    private static SharedPreferences sharedPreferences;//共享首选项
    public static final String SHARE_NAEM = "share";//不用加后缀.xml


    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    public static Map getSharePreferences(Context context){

        sharedPreferences = context.getSharedPreferences(SHARE_NAEM, Context.MODE_PRIVATE);
        Map map = new HashMap();
        map.put(ID,sharedPreferences.getInt(ID,0));
        map.put(USERNAME,sharedPreferences.getString(USERNAME,null));
        map.put(PASSWORD,sharedPreferences.getString(PASSWORD,null));
        return map;
    }

    public static void setSharePreferences(Context context,int id, String username, String password){

        sharedPreferences = context.getSharedPreferences(SHARE_NAEM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID,id);
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        editor.commit();

    }


}
