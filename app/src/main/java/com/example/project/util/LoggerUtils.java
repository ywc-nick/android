package com.example.project.util;

import android.util.Log;

/**
 * 日志工具类
 */
public class LoggerUtils {

    private static final String TAG = "MyAppLogger";

    public static void d(String message) {
        Log.d(TAG, message);
    }
    public static void d(String title,String message) {
        Log.d(TAG+title, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String title,String message) {
        Log.e(TAG+title, message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void i(String title,String message) {
        Log.i(TAG+title, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void w(String title,String message) {
        Log.w(TAG+title, message);
    }
}