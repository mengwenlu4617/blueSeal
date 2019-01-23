package com.fastwork.library.mutils;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.Button;

import com.fastwork.library.R;

/**
 * Created by lenovo on 2019/1/7.
 * 倒计时实现
 */

public class TimeCountUtil extends CountDownTimer {

    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to {@link #start()} until the countdown is done and {@link #onFinish()}
     * is called.
     * @param countDownInterval The interval along the way to receive
     * {@link #onTick(long)} callbacks.
     */
    private Button mButton;

    public TimeCountUtil(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mButton = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // 按钮不可用
        mButton.setEnabled(false);
        String showText = millisUntilFinished / 1000 + " 秒";
        mButton.setText(showText);
    }

    @Override
    public void onFinish() {
        // 按钮设置可用
        mButton.setEnabled(true);
        mButton.setText("重新发送");
    }
}
