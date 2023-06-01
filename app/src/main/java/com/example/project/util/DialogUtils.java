package com.example.project.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class DialogUtils {

    /**
     * 创建一个 Toast 提示
     *
     * @param context  上下文对象
     * @param message  提示消息
     * @param duration 提示持续时间（例如 Toast.LENGTH_SHORT）
     */
    public static void showShortToast(Context context, String message, int duration) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context, String message, int duration) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 创建一个 Snackbar 提示
     *
     * @param view         视图
     * @param message      提示消息
     * @param duration     提示持续时间（例如 Snackbar.LENGTH_SHORT）
     * @param actionText   操作文本
     * @param clickListener 操作执行的监听器
     */
    public static void showSnackbar(View view, String message, int duration, String actionText, View.OnClickListener clickListener) {
        Snackbar.make(view, message, duration)
                .setAction(actionText, clickListener)
                .show();
    }

    /**
     * 创建一个进度对话框
     *
     * @param context 上下文对象
     * @param message 对话框消息
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 创建一个确认对话框
     *
     * @param context  上下文对象
     * @param title    标题
     * @param message  提示消息
     * @param listener 确认操作执行的监听器
     */
    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    public static void showSingleConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener);
        builder.create().show();
    }
}