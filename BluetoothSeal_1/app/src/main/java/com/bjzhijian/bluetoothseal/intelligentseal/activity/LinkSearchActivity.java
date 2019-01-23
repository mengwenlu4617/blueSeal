package com.bjzhijian.bluetoothseal.intelligentseal.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseActivity;
import com.bjzhijian.bluetoothseal.intelligentseal.views.CircleProgressView;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;
import com.fastwork.library.listener.OnMultiClickListener;
import com.fastwork.library.mutils.MLogUtil;

import butterknife.BindView;

/**
 * Created by lenovo on 2019/1/15.
 * 配网----》》》》 搜索印章配网
 */

public class LinkSearchActivity extends BaseActivity {

    @BindView(R.id.title_left_layout)
    FrameLayout titleLeftLayout;
    @BindView(R.id.title_center_text)
    MTextView titleCenterText;
    @BindView(R.id.linkSearch_circleProgress)
    CircleProgressView linkSearchCircleProgress;
    private MyCountDownTimer downTimer;
    private long millisInFuture = 50000;
    private int countDownInterval = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_linksearch_layout;
    }

    @Override
    protected void initData() {
        titleCenterText.setText(getString(R.string.search));
    }

    @Override
    protected void setListener() {
        titleLeftLayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finishActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (downTimer == null) {
            downTimer = new MyCountDownTimer(millisInFuture, countDownInterval);
            downTimer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (downTimer != null) {
            downTimer.cancel();
            downTimer = null;
        }
    }

    private class MyCountDownTimer extends CountDownTimer {

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            linkSearchCircleProgress.setProgress((int) (millisInFuture - millisUntilFinished) / countDownInterval);
        }

        @Override
        public void onFinish() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishActivity();
                }
            }, 200);
        }
    }

}
