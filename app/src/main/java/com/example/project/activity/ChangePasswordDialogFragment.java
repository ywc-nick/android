package com.example.project.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.project.R;

public class ChangePasswordDialogFragment extends DialogFragment {


    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    SendPassword sendPassword;
    String password;

    public ChangePasswordDialogFragment(SendPassword sendPassword) {
        this.sendPassword = sendPassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change, container, false);
        password = getArguments().getString("password");
        // 初始化布局中的控件

        oldPasswordEditText = view.findViewById(R.id.OldPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        Button confirmButton = view.findViewById(R.id.confirmButton);

        // 设置确认按钮的点击事件
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldpassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                //添加  判断旧密码
                if(oldpassword.equals(password)){
                    if (newPassword.equals(confirmPassword)) {
                        // 执行密码修改操作，这里可以根据需要进行具体处理
                        sendPassword.sendPassword(newPassword);
                        password = newPassword;
                        Toast.makeText(getActivity(), "密码已更新", Toast.LENGTH_SHORT).show();
                        dismiss(); // 关闭对话框
                    } else {
                        Toast.makeText(getActivity(), "密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置对话框的宽度和高度，可以根据需要进行调整
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public interface SendPassword{
        void sendPassword(String password);
    }
}