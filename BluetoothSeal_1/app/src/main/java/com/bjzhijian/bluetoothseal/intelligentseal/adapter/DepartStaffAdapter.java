package com.bjzhijian.bluetoothseal.intelligentseal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.model.DepartmentModel;
import com.bjzhijian.bluetoothseal.intelligentseal.model.StaffModel;
import com.bjzhijian.bluetoothseal.intelligentseal.widgets.MTextView;

import java.util.List;

/**
 * Created by lenovo on 2019/1/2.
 * 部门--》》 员工 适配
 */

public class DepartStaffAdapter extends BaseAdapter {

    private Context mContext;
    private List<StaffModel> mList;
    public DepartStaffAdapter(Context context, List<StaffModel> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.depart_import_item_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StaffModel model = mList.get(position);
        if (model != null) {
            holder.text.setText(model.getName());
            if (model.isSelect()){
                holder.checkBox.setChecked(true);
            }else {
                holder.checkBox.setChecked(false);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        CheckBox checkBox;
        MTextView text;

        ViewHolder(View view) {
            checkBox = view.findViewById(R.id.depart_import_item_cb);
            text = view.findViewById(R.id.depart_import_item_tvName);
        }
    }
}
