package com.example.project.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 该OkHttp工具类提供了三种HTTP请求的方法，分别是GET、POST表单、POST Json。
 * 这些方法的参数中都包含回调函数Callback，用于处理异步请求的回调结果。
 * 如果想要进行同步请求，可以使用execute方法。
 */
public class OkHttpUtil {

    //url前缀只需后面添加
    public static String baseUrl = "http://10.0.2.2:8080/qianxun/app";

    public static String NO = "no";//返回的响应
    public static String OK = "ok";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor())
            .build();

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void delete(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void postForm(String url, FormBody formBody, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getJson(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postJson(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void putJson(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteJson(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json;charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .delete(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 同步
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String execute(Request request) {

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpLoggingInterceptor loggingInterceptor() {
        //日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }



    //        // 向服务器发送 HTTP 请求
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://example.com/api/update")  // 假设服务器 API 地址为 http://example.com/api/update
//                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mDataList.toString()))
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            if (response.isSuccessful()) {
//                // 更新成功
//            } else {
//                // 更新失败，处理异常
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;

}