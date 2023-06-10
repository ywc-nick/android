package com.example.project.util;

import com.example.project.pojo.Custer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public static void putFormAddImage(String url, String filename, Callback callback) {
        File file = new File(filename);

        byte[] data = new byte[(int) file.length()];
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            input.read(data);
            input.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), data))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)//一般填充请求体
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postFormAddImage(String url, String filename, Custer custer, Callback callback) {

//        (@RequestParam(value = "image",defaultValue = "null") MultipartFile image,
//                @RequestParam(value = "username",defaultValue = "null") String username,
//                @RequestParam(value = "password",defaultValue = "null") String password,
//                @RequestParam(value = "nickname",defaultValue = "null") String nickname,
//                @RequestParam(value = "imageName",defaultValue = "null") String imageName,
//                @RequestParam(value = "sex",defaultValue = "null") String sex,
//                @RequestParam(value = "birth",defaultValue = "null") String birth,
//                @RequestParam(value = "phone",defaultValue = "null") String phone)
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (filename != null) {
            File file = new File(filename);
            byte[] data = new byte[(int) file.length()];
            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                input.read(data);
                input.close();
            }catch (IOException e) {
                e.printStackTrace();
            }

            builder.addFormDataPart("image", file.getName(),
                    RequestBody.create(MediaType.parse("image/jpeg"), data))
                    .addFormDataPart("imageName", file.getName());//传递特定用户专属文件;

        } else {
            url = url + "/no";
        }

        if (custer.getUsername() != null) {
            builder.addFormDataPart("username", custer.getUsername());
        }
        if (custer.getName() != null) {
            builder.addFormDataPart("name", custer.getName());
        }
        if (custer.getPassword() != null) {
            builder.addFormDataPart("password", custer.getPassword());
        }
        if (custer.getVir_name() != null) {
            builder.addFormDataPart("nickname", custer.getVir_name());
        }
        if (custer.getSex() != null) {
            builder.addFormDataPart("sex", custer.getSex());
        }
        if (custer.getBirth() != null) {
            builder.addFormDataPart("birth", custer.getBirth());
        }
        if (custer.getPhone() != null) {
            builder.addFormDataPart("phone", custer.getPhone());
        }

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

}
