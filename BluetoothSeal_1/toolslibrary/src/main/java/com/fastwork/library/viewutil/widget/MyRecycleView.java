package com.fastwork.library.viewutil.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by lenovo on 2019/1/15.
 * 统一设置 LinearLayoutManager
 */

public class MyRecycleView extends RecyclerView{

    public MyRecycleView(Context context) {
        super(context);
        init();
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        setLayoutManager(linearLayoutManager);
    }
}
