package com.example.project.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 该OkHttp工具类提供了三种HTTP请求的方法，分别是GET、POST表单、POST Json。
 * 这些方法的参数中都包含回调函数Callback，用于处理异步请求的回调结果。
 * 如果想要进行同步请求，可以使用execute方法。
 */
public class OkHttpUtil {

//url前缀只需后面添加
  public static String baseUrl = "http://10.0.2.2:8080/qianxun/app";

  private static final OkHttpClient client = new OkHttpClient();

  public static void get(String url, Callback callback) {
    Request request = new Request.Builder()
        .url(url)
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
    RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();
    client.newCall(request).enqueue(callback);
  }

  public static void postJson(String url, String json, Callback callback) {
    RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
    Request request = new Request.Builder()
        .url(url)
        .post(requestBody)
        .build();
    client.newCall(request).enqueue(callback);
  }

  public static void putJson(String url, String json, Callback callback) {
    RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
    Request request = new Request.Builder()
            .url(url)
            .put(requestBody)
            .build();
    client.newCall(request).enqueue(callback);
  }
  public static void deleteJson(String url, String json, Callback callback) {
    RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
    Request request = new Request.Builder()
            .url(url)
            .delete(requestBody)
            .build();
    client.newCall(request).enqueue(callback);
  }

  /**
   * 同步
   * @param request
   * @return
   * @throws IOException
   */
  public static String execute(Request request) throws IOException {
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

}