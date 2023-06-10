package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.pojo.Custer;
import com.example.project.util.DialogUtils;
import com.example.project.util.FileUtils;
import com.example.project.util.ImageUtils;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.example.project.util.TimeUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InfoActivity extends AppCompatActivity {

    ImageView cancle, image;
    TextView save, sex, birth;
    EditText name, nickname;
    String imagePath=null;
    Custer custer;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        init();
        dataFill();
    }

    public void init() {
        cancle = findViewById(R.id.act_info_cancel);
        image = findViewById(R.id.act_info_image);
        save = findViewById(R.id.act_info_save);
        sex = findViewById(R.id.act_info_sex);
        name = findViewById(R.id.act_info_name);
        nickname = findViewById(R.id.act_info_nickname);
        birth = findViewById(R.id.act_info_birth);

        birth.setOnClickListener(listener);
        cancle.setOnClickListener(listener);
        image.setOnClickListener(listener);
        save.setOnClickListener(listener);
        sex.setOnClickListener(listener);
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    name.setText(custer.getName());
                    nickname.setText(custer.getVir_name());
                    if (custer.getBirth()!=null){
                        birth.setText(TimeUtils.TimestampToDateString2(custer.getBirth()));
                    }
                    String sexVal = custer.getSex();
                    if ("1".equals(sexVal)) {
                        sex.setText("男");
                        yourChoice = 1;
                    } else if ("2".equals(sexVal)) {
                        sex.setText("女");
                        yourChoice = 2;
                    } else {
                        yourChoice = 0;
                        sex.setText("保密");
                    }
                    if (custer.getImagebytes() != null) {
                        image.setImageBitmap(ImageUtils.getRoundedCornerBitmap(custer.getImagebytes(), ImageUtils.bigimage));
                    }
                    break;
                case 2:
                    Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    Toast.makeText(InfoActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.act_info_cancel:
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
                    DialogUtils.showConfirmDialog(InfoActivity.this, null, "确认返回吗？当前的资料修改尚未保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
//                        }
//                    });

                    break;
                case R.id.act_info_image:
                    imageUpload();
                    break;
                case R.id.act_info_save:
                    dataSave();
                    break;
                case R.id.act_info_sex:
                    showSingleChoiceDialog();
                    break;
                case R.id.act_info_birth:
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    // 显示日期选择器对话框
                    DatePickerDialog datePickerDialog = new DatePickerDialog(InfoActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
//                            android.R.style.,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                    // 在 EditText 中显示所选日期
                                    birth.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth);
                                }
                            },
                            year, month, day);
                    datePickerDialog.getDatePicker().setCalendarViewShown(false); // 隐藏日历视图
                    datePickerDialog.show();
                    break;
            }
        }
    };


    int yourChoice = 0;

    private void showSingleChoiceDialog() {
        final String[] items = {"保密", "男", "女"};

        androidx.appcompat.app.AlertDialog.Builder singleChoiceDialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        singleChoiceDialog.setTitle("请选择");
        singleChoiceDialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                yourChoice = i;
            }
        });
        singleChoiceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sex.setText(items[yourChoice].toString());
            }
        });
        singleChoiceDialog.show();
    }


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
                Intent intent;
                switch (which) {
                    case 0:
                        // 启动相机拍照
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 0);
                        break;
                    case 1:
                        // 从相册选择照片
                        builder.create().show();
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);
                        break;
                }
            }
        });
        builder.create().show();
        return;
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
                    image.setImageBitmap(bitmap);
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
                    imagePath = path;//记录文件路径
                    Log.i("拍照存储路径", path);
                    break;
                case 1:
                    Uri uri = data.getData();
                    image.setImageURI(uri);
                    imagePath = FileUtils.getPath(this, uri);
                    Log.i("照片存储位置", imagePath);
                    Bitmap bitMap = null;
                    try {
                        bitMap = ImageUtils.getBitMap(imagePath);
                        image.setImageBitmap(bitMap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void dataSave() {
        Map map = SharedPreferencesUtils.getSharePreferences(this);
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);
        if (id == 0) {
            return;
        }
        String url = OkHttpUtil.baseUrl + "/custer/update/image/"+id;
        //获取EditText数据
        String nameVal = name.getText().toString();
        String sexVal = yourChoice + "";
        String nickVal = nickname.getText().toString();
        String birthVal = birth.getText().toString();
        //2.配置request:添加数据+请求方法+url
        Custer custer = new Custer(id, null, null, nameVal, nickVal, imagePath, sexVal, birthVal, null, null, null);

        if (imagePath != null) {
            OkHttpUtil.putFormAddImage( url, imagePath, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    LoggerUtils.i(res);
                    if ("ok".equals(res)) {
                        Message message = mHandler.obtainMessage(3);
                        mHandler.sendMessage(message);
                    }
                }
            });
        }
        url = OkHttpUtil.baseUrl + "/custer/update/info";
        Gson gson = new Gson();
        String json = gson.toJson(custer, Custer.class);
        OkHttpUtil.putJson(url, json, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                LoggerUtils.i(res);

                if ("ok".equals(res)) {
                    Message message = mHandler.obtainMessage(2);
                    mHandler.sendMessage(message);
                }
            }
        });


    }


    public void dataFill() {
        Map map = SharedPreferencesUtils.getSharePreferences(this);
        Integer id = (Integer) map.get(SharedPreferencesUtils.ID);
        if (id == 0) {
            return;
        }
        String url = OkHttpUtil.baseUrl + "/custer/" + id;
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
                try {
                    Gson gson = new Gson();
                    custer = gson.fromJson(info, Custer.class);
                    Message message = mHandler.obtainMessage(1);
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    LoggerUtils.e("MyFragment数据为空", e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}