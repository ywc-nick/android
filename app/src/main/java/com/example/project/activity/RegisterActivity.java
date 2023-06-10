package com.example.project.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.pojo.Custer;
import com.example.project.util.FileUtils;
import com.example.project.util.ImageUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_nicke;
    private EditText et_phone;
    private EditText et_password, birth;
    //    RadioGroup sex;
//    private RadioButton boy,girl;
    private Button login1;
    private Button register1;
    ImageView imageView;
//    LinearLayout image;


    Gson gson = new Gson();
    Intent intent = null;
    private String sexVal = "1";//1 男 2女 默认为男

    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }


    private void init() {
        et_name = findViewById(R.id.et_name);
        et_nicke = findViewById(R.id.et_nicke);
        et_password = findViewById(R.id.et_password);
        et_phone = findViewById(R.id.et_phone);
//        sex = findViewById(R.id.rg_sex);
//        boy = findViewById(R.id.rb_man);
//        girl = findViewById(R.id.rb_woman);
//
//        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.rb_man:
//                        sexVal = "1";
//                        break;
//                    case R.id.rb_woman:
//                        sexVal = "2";
//                        break;
//                }
//            }
//        });

        login1 = findViewById(R.id.btn_login1);
        register1 = findViewById(R.id.btn_register1);
//        image = findViewById(R.id.act_register_image);
        imageView = findViewById(R.id.imageView);
//        birth = findViewById(R.id.et_birth);

        //点击按钮返回到登录界面
        login1.setOnClickListener(listener);
        //点击注册按钮跳转到登录界面
        register1.setOnClickListener(listener);
        imageView.setOnClickListener(listener);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register1:
                    register();
                    break;
                case R.id.btn_login1:
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.imageView:
                    imageUpload();
                    break;
            }
        }
    };

    private void imageUpload() {
        //赋予高级权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择照片来源");
        builder.setItems(new CharSequence[]{"拍照", "选择相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 启动相机拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        // 从相册选择照片
                        builder.create().show();
                        Intent intent2 = new Intent();
                        intent2.setAction(Intent.ACTION_GET_CONTENT);
                        intent2.setType("image/*");
                        startActivityForResult(intent2, 1);
                        break;
                }
            }
        });
        builder.create().show();

    }

    /**
     * 图片剪裁
     *
     * @param uri 图片uri
     */
    private void pictureCropping(Uri uri) {
        // 创建保存剪裁结果的 Uri
//        Uri cropUri = Uri.fromFile(new File(getCacheDir(), "crop_image.jpg"));
//
//        // 创建剪裁请求
//        UCrop uCrop = UCrop.of(uri, cropUri)
//                .withAspectRatio(1, 1)  // 设置剪裁比例为 1:1
//                .withMaxResultSize(500, 500);  // 设置最大的剪裁结果尺寸
//
//        // 启动剪裁界面并接收剪裁结果
//        uCrop.start(this);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    imageView.setImageBitmap(bitmap);
                    String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
                    String path = getApplicationContext().getCacheDir().getAbsolutePath() + "/images/" + fileName;
                    FileOutputStream b = null;
                    File file = new File(path);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();// 创建文件夹
                    }
                    try {
                        b = new FileOutputStream(path);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, b);
                        b.flush();
                        b.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("-------------", "path:" + path);
                    imagePath=path;//记录文件路径
                    Log.i("拍照存储路径", path);
                    break;
                case 1:
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    imagePath = FileUtils.getPath(this, uri);
                    Log.i("照片存储位置", imagePath);
                    Bitmap bitMap = null;
                    try {
                        bitMap = ImageUtils.getBitMap(imagePath);
                        imageView.setImageBitmap(bitMap);
//            imageByte = bitMap;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//                    pictureCropping(uri);
                    break;


            }
        }
    }

    private void register() {
        String url = OkHttpUtil.baseUrl + "/custer/add";
        //获取EditText数据
        String username = et_name.getText().toString();
        String nickname = et_nicke.getText().toString();
        String phone = et_phone.getText().toString();
        String pass = et_password.getText().toString();
//        String birthVal =birth.getText().toString() ;

        //2.配置request:添加数据+请求方法+url
        Custer custer = new Custer(username, pass, null, nickname, imagePath, null, null, phone, null);
        OkHttpUtil.postFormAddImage(url, imagePath, custer, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                LoggerUtils.i(res);
                if ("ok".equals(res)) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("username", username);
//                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });

    }

}
//接受no和ok



