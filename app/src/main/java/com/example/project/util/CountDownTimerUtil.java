package com.example.project.util;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 计时器
 */
public class CountDownTimerUtil extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    Button timeBtn;

    public CountDownTimerUtil(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        timeBtn = button;
    }


    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        timeBtn.setClickable(false);
        timeBtn.setText(l/1000+"秒");

    }


    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        timeBtn.setText("重新获取");
        //设置可点击
        timeBtn.setClickable(true);
    }



    public void cancle() {
        if (this != null) {
            this.cancel();
        }
    }
}
