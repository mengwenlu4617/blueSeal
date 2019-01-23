package com.bjzhijian.bluetoothseal.intelligentseal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.bjzhijian.bluetoothseal.intelligentseal.base.BaseViewHolder;
import com.bjzhijian.bluetoothseal.intelligentseal.base.RecycleViewItemData;
import com.bjzhijian.bluetoothseal.intelligentseal.callback.HomeAdapterListener;
import com.bjzhijian.bluetoothseal.intelligentseal.model.RecordModel;
import com.fastwork.library.listener.OnMultiClickListener;

import java.util.List;

/**
 * Created by lenovo on 2019/1/15.
 * 首页适配
 */

public class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private HomeAdapterListener adapterListener;
    private static final int TYPE_EDIT = 0;// banner
    private static final int TYPE_BUTTON = 1;// 按钮
    private static final int TYPE_SPINNER = 2;// 列表
    private static final int TYPE_EMPTY = 3;// 空数据
    private List<RecycleViewItemData> dataList;// 数据集合

    public HomeAdapter(Context context, List<RecycleViewItemData> list) {
        this.mContext = context;
        this.dataList = list;
    }

    public void setData(List<RecycleViewItemData> list){
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void setAdapterListener(HomeAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == dataList.get(position).getDataType()) {
            return TYPE_EDIT;// banner
        } else if (1 == dataList.get(position).getDataType()) {
            return TYPE_BUTTON;// 按钮
        } else if (2 == dataList.get(position).getDataType()) {
            return TYPE_SPINNER;// 列表
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EDIT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_header_banner_layout, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == TYPE_BUTTON) {  //如果viewType是按钮类型,则创建ButtonViewHolder型viewholder
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_header_buttom_layout, parent, false);
            return new ButtonViewHolder(view);
        } else if (viewType == TYPE_SPINNER) { //如果viewType是下拉列表类型,则创建SpinnerHolder型viewholder
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_item_view_layout, parent, false);
            return new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).imageView.setOnClickListener(clickListener);
        } else if (holder instanceof ButtonViewHolder) {
            ((ButtonViewHolder) holder).view1.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view2.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view3.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view4.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view5.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view6.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view7.setOnClickListener(clickListener);
            ((ButtonViewHolder) holder).view8.setOnClickListener(clickListener);
        } else if (holder instanceof ItemViewHolder) {
            RecordModel model = (RecordModel) dataList.get(position).getObject();
            if (model != null){
                ((ItemViewHolder) holder).tvName.setText(model.getDeviceName());
//                ((ItemViewHolder) holder).tvReason.setText(model.getReson());
                ((ItemViewHolder) holder).tvTime.setText(model.getCreateTime());
            }
        }
    }

    private OnMultiClickListener clickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            if (adapterListener != null){
                adapterListener.channelClick(v.getId());
            }
        }
    };

    /**
     * 图片
     **/
    class ImageViewHolder extends BaseViewHolder {
        private ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_header_banner_image);
        }
    }

    /**
     * 快速选择按钮
     **/
    class ButtonViewHolder extends BaseViewHolder {
        private View view1, view2, view3, view4;
        private View view5, view6, view7, view8;

        ButtonViewHolder(View itemView) {
            super(itemView);
            view1 = itemView.findViewById(R.id.home_header_channel_view1);
            view2 = itemView.findViewById(R.id.home_header_channel_view2);
            view3 = itemView.findViewById(R.id.home_header_channel_view3);
            view4 = itemView.findViewById(R.id.home_header_channel_view4);
            view5 = itemView.findViewById(R.id.home_header_channel_view5);
            view6 = itemView.findViewById(R.id.home_header_channel_view6);
            view7 = itemView.findViewById(R.id.home_header_channel_view7);
            view8 = itemView.findViewById(R.id.home_header_channel_view8);
        }
    }

    /**
     * Item
     **/
    class ItemViewHolder extends BaseViewHolder {
        private View clickView;
        private ImageView item_image;
        private TextView tvName, tvReason, tvType, tvTime;

        ItemViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.home_item_clickView);
            item_image = (ImageView) itemView.findViewById(R.id.home_item_image);
            tvName = (TextView) itemView.findViewById(R.id.home_item_tvName);
            tvReason = (TextView) itemView.findViewById(R.id.home_item_tvReason);
            tvType = (TextView) itemView.findViewById(R.id.home_item_tvType);
            tvTime = (TextView) itemView.findViewById(R.id.home_item_tvTime);
        }
    }
}
