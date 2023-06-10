package com.example.project.util;

import com.example.project.pojo.Custer;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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


    public static void postForm(String url, FormBody.Builder formBody, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postFormAddImage(String url, String filename, Custer custer, Callback callback) {

//        @RequestParam("username") String username,
//        @RequestParam("password") String password, @RequestParam("nickname") String nickname,
//        @RequestParam("imagePath") String imagePath,@RequestParam("phone") String phone)
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (filename!=null) {
            File file = new File(filename);
            builder.addFormDataPart("image", filename,
                    RequestBody.create(MediaType.parse("multipart/form-data"), file.getName()))
                    .addFormDataPart("imageName", file.getName());//传递特定用户专属文件;

        }else {
            url = url+"/no";
        }
        builder.addFormDataPart("username", custer.getUsername())//传递特定用户专属文件
                .addFormDataPart("password", custer.getPassword())//传递特定用户专属文件
                .addFormDataPart("nickname", custer.getVir_name())//传递特定用户专属文件
                .addFormDataPart("phone", custer.getPhone());//传递特定用户专属文件
        requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)//一般填充请求体
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
//    public static void postFormAddImage(String url,String file, Custer custer, Callback callback) {
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image",file,
//                        RequestBody.create(MediaType.parse("multipart/form-data"),new File(file))
//                )
//                .addFormDataPart("custer", String.valueOf(custer))//传递特定用户专属文件
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)//一般填充请求体
//                .build();
//        client.newCall(request).enqueue(callback);
//    }
//
//
//    // 获取 OkHttpClient 实例
//    OkHttpClient client = ((MyApplication) getApplication()).getClient();
//    // 创建 JSON 对象
//    JSONObject custerJson = new JSONObject();
//custerJson.put("name", "张三");
//        custerJson.put("age", 25);
//// 创建 JSON 请求体
//        RequestBody custerBody = RequestBody.create(MediaType.parse("application/json"), custerJson.toString());
//// 创建文件请求体
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
//// 创建 MultipartBody.Part 对象
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), fileBody);
//// 创建 MultipartBody 对象
//        MultipartBody requestBody = new MultipartBody.Builder()
//        .setType(MultipartBody.FORM)
//        .addPart(filePart)
//        .addFormDataPart("custer", custerJson.toString())
//        .build();
//// 创建请求对象
//        Request request = new Request.Builder()
//        .url(url)
//        .post(requestBody)
//        .build();
//// 发送请求
