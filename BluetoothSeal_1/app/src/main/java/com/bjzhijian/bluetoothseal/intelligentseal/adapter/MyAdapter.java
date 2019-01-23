package com.bjzhijian.bluetoothseal.intelligentseal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.model.MineItemModel;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;

import java.util.List;

/**
 * Created by lenovo on 2019/1/2.
 * 我的适配
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private List<MineItemModel> mList;
    public MyAdapter(Context context, List<MineItemModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_item_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MineItemModel model = mList.get(position);
        if (model != null) {
            holder.leftImg.setImageResource(model.getImgId());
            holder.text.setText(model.getText());
            if (model.isGroup()){
                holder.emptyView.setVisibility(View.VISIBLE);
            }else {
                holder.emptyView.setVisibility(View.GONE);
            }
            if (model.isRightImg()){
                holder.rightImg.setVisibility(View.VISIBLE);
            }else {
                holder.rightImg.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView leftImg,rightImg;
        MTextView text;
        View emptyView;

        ViewHolder(View view) {
            leftImg = view.findViewById(R.id.my_item_img);
            text = view.findViewById(R.id.my_item_text);
            emptyView = view.findViewById(R.id.my_item_view);
            rightImg = view.findViewById(R.id.my_item_rightImg);
        }
    }
}
