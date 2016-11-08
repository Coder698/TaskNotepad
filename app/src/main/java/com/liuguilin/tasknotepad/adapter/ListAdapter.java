package com.liuguilin.tasknotepad.adapter;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.adapter
 *  文件名:   ListAdapter
 *  创建者:   LGL
 *  创建时间:  2016/10/24 15:33
 *  描述：    适配器
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.liuguilin.tasknotepad.R;
import com.liuguilin.tasknotepad.entity.DataBean;

import com.liuguilin.tasknotepad.utils.LogUtils;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<DataBean> mList;

    public ListAdapter(Context mContext, List<DataBean> mList) {
        this.mContext = mContext;
        this.mList = mList;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.tv_day = (TextView) convertView.findViewById(R.id.day);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.content);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.mCheckBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = mList.get(position);
        viewHolder.tv_day.setText(bean.getDay());
        viewHolder.tv_time.setText(bean.getTime());
        viewHolder.tv_msg.setText(bean.getMsg());

        if(mList.get(position).isShowCheckBox()){
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            if(mList.get(position).isSelect()){
                viewHolder.mCheckBox.setChecked(true);
            }else {
                viewHolder.mCheckBox.setChecked(false);
            }
        }else {
            viewHolder.mCheckBox.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_day;
        private TextView tv_time;
        private TextView tv_msg;
        private CheckBox mCheckBox;
    }
}
