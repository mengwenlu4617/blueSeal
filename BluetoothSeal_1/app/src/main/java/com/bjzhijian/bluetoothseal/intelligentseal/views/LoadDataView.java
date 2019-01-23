package com.bjzhijian.bluetoothseal.intelligentseal.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;

/**
 * Created by lenovo on 2018/12/27.
 * 加载数据状态
 */

public class LoadDataView extends LinearLayout {

    private LinearLayout loadStatusView;
    private ImageView image;
    private MTextView text;

    public LoadDataView(Context context) {
        super(context);
        init(context);
    }

    public LoadDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     **/
    private void init(Context context) {
        View mView = LayoutInflater.from(context).inflate(R.layout.load_status_layout, this);
        loadStatusView = mView.findViewById(R.id.loadStatus_view);
        image = mView.findViewById(R.id.loadStatus_image);
        text = mView.findViewById(R.id.loadStatus_text);
    }

    /**
     * 不显示
     **/
    public void notShowView() {
        loadStatusView.setVisibility(View.GONE);
    }

    /**
     * 没有数据
     **/
    public void loadNoData() {
        loadStatusView.setVisibility(View.VISIBLE);
        image.setImageResource(R.mipmap.icon_empty);
        text.setText(R.string.no_data);
    }

    /**
     * 没有网络
     **/
    public void loadNoNet() {
        loadStatusView.setVisibility(View.VISIBLE);
        image.setImageResource(R.mipmap.icon_neterror);
        text.setText(R.string.no_net);
    }

}
