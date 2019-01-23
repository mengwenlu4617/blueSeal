package com.bjzhijian.bluetoothseal.intelligentseal.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;

import com.bjzhijian.bluetoothseal.intelligentseal.R;

/**
 * Created by lenovo on 2018/12/25.
 * 重新定义 Dialog
 */

public class LoadDialogView extends Dialog {

    public LoadDialogView(@NonNull Context context) {
        super(context, R.style.loadDialog_style);
        init();
    }

    private void init() {
        setContentView(R.layout.load_progress_view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        //设置不可取消
        //点击其他区域不能取消
//        setCanceledOnTouchOutside(true); 这里是没有效果的，因为范围是全屏
        findViewById(R.id.loadProgress_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        if (!this.isShowing()) {
            super.show();
        }
//        handler.postDelayed(runnable,20000);
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            super.dismiss();
        }
    }
//
//    private Handler handler= new Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            dismiss();
//            handler.removeCallbacks(runnable);
//        }
//    };
}
