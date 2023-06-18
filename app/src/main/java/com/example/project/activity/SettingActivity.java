package com.example.project.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.pojo.Custer;
import com.example.project.util.LoggerUtils;
import com.example.project.util.OkHttpUtil;
import com.example.project.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity implements ChangePasswordDialogFragment.SendPassword{
    TextView manage, change, telephone;
    Button logout;
    ImageView back;

    Custer custer;
    Gson gson = new Gson();
    Integer id;
    String pass,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Map map = SharedPreferencesUtils.getSharePreferences(this);
        id = (Integer) map.get(SharedPreferencesUtils.ID);
        init();
        dataFill();

    }

    public void init() {
        manage = findViewById(R.id.manage);
        change = findViewById(R.id.change);
        telephone = findViewById(R.id.telephone);
        logout = findViewById(R.id.act_LogOut);
        back = findViewById(R.id.act_my_text_return);

        logout.setOnClickListener(listener);
        back.setOnClickListener(listener);
        telephone.setOnClickListener(listener);
        change.setOnClickListener(listener);


    }

    View.OnClickListener listener = new View.OnClickListener() {
        Intent intent;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.act_my_text_return:
                    finish();
                    break;
                case R.id.act_LogOut:
                    intent = new Intent(SettingActivity.this, RegisterActivity.class);
                    break;
                case R.id.telephone:
                    showInputDialog();
                    break;
                case R.id.change:
                    showChangeDialog();
                    break;
                default:
                    break;

            }
        }
    };


    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入你的电话号码");

        // 创建用于输入的EditText
        final EditText inputEditText = new EditText(this);
        builder.setView(inputEditText);

        // 设置确定和取消按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userInput = inputEditText.getText().toString();
                // 处理用户输入的逻辑
                phone = userInput;
                putData();
                telephone.setText(phone);
                Toast.makeText(SettingActivity.this, "你输入的号码是：" + userInput, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        // 显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showChangeDialog() {
//        ChangePasswordDialogFragment dialog = ChangePasswordDialogFragment.newInstance(custer.getPassword());
//        dialog.show(getSupportFragmentManager(), "ChangePasswordDialog");
        ChangePasswordDialogFragment dialog = new ChangePasswordDialogFragment(this);
        Bundle args = new Bundle();
        args.putString("password", custer.getPassword());
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "ChangePasswordDialog");
    }



    public void dataFill() {

        if (id == 0) {
            return;
        }

        String url = OkHttpUtil.baseUrl + "/custer/" + id;
        OkHttpUtil.get(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String info = response.body().string().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            custer = gson.fromJson(info, Custer.class);
                            if (custer.getUsername()!=null){
                                manage.setText(custer.getUsername());
                            }
                            if (custer.getPhone()!=null){
                                telephone.setText(custer.getPhone());
                                phone = custer.getPhone();
                            }
                            pass = custer.getPassword();


                        } catch (Exception e) {
                            LoggerUtils.e("MyFragment数据为空", e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LoggerUtils.i("数据获取失败！");
            }
        });
    }


    @Override
    public void sendPassword(String password) {
        pass = password;
        putData();

    }

    public void putData(){
        Custer custer1 = new Custer();
        custer1.setId(id);
        custer1.setPassword(pass);
        custer1.setPhone(phone);

        String url = OkHttpUtil.baseUrl + "/custer/update/info";
        Gson gson = new Gson();
        String json = gson.toJson(custer1, Custer.class);
        OkHttpUtil.putJson(url, json, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("ok".equals(res)) {
                            SharedPreferencesUtils.updatePass(SettingActivity.this,pass);
                            Toast.makeText(SettingActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SettingActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }


}
