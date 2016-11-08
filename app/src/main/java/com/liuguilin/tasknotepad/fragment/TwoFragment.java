package com.liuguilin.tasknotepad.fragment;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.fragment
 *  文件名:   OneFragment
 *  创建者:   LGL
 *  创建时间:  2016/10/24 13:31
 *  描述：    two
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.liuguilin.tasknotepad.R;
import com.liuguilin.tasknotepad.adapter.ListAdapter;
import com.liuguilin.tasknotepad.db.TaskDBHelper;
import com.liuguilin.tasknotepad.entity.ActionEvent;
import com.liuguilin.tasknotepad.entity.DataBean;
import com.liuguilin.tasknotepad.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoFragment extends Fragment implements View.OnClickListener,
    AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    private ListView taskListView;
    private ListAdapter mAdapter;
    private List<DataBean> mList = new ArrayList<>();
    private SQLiteDatabase db;

    private LinearLayout bottom_layout;     // 下面隐藏的布局
    private Button btn_selectAll,btn_delect,btn_cancel; //全选，删除，取消
    private boolean isLineaLayoutVisible = false;   // 标记按钮布局的显示

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, null);
        TaskDBHelper hepler = new TaskDBHelper(getActivity());
        db = hepler.getWritableDatabase();
        EventBus.getDefault().register(this);//注册
        findView(view);
        getTaskInfo();
        return view;
    }

    //初始化
    private void findView(View view) {
        bottom_layout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        btn_selectAll = (Button) view.findViewById(R.id.btn_selectAll);
        btn_delect = (Button) view.findViewById(R.id.btn_delect);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_selectAll.setOnClickListener(this);
        btn_delect.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        taskListView = (ListView) view.findViewById(R.id.taskListView);
        taskListView.setOnItemLongClickListener(this);
        taskListView.setOnItemClickListener(this);
        //倒序
        Collections.reverse(mList);
        mAdapter = new ListAdapter(getActivity(), mList);
        taskListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
    /**
     * 接收消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(ActionEvent event) {
        if(event.getType().equals("button")){
            mList.addAll(event.getList());
            //倒序
            Collections.reverse(mList);
            mAdapter.notifyDataSetChanged();


        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//注销
    }



    /**
     * 查询所有记录的任务数据
     */
    private void getTaskInfo(){
        TaskDBHelper helper = new TaskDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from taskinfo order by id desc";
        try {
            Cursor c = db.rawQuery(sql,null);
            while (c.moveToNext()){
                DataBean bean = null;
                int id = c.getInt(c.getColumnIndex("id"));
                String day = c.getString(c.getColumnIndex("day"));
                String time = c.getString(c.getColumnIndex("time"));
                String content = c.getString(c.getColumnIndex("content"));
                bean = new DataBean();
                bean.setId(id);
                bean.setDay(day);
                bean.setTime(time);
                bean.setMsg(content);
                bean.setSelect(false);
                bean.setShowCheckBox(false);
                mList.add(bean);
                mAdapter = new ListAdapter(getActivity(), mList);
                taskListView.setAdapter(mAdapter);
            }
            c.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    /**
     * 隐藏Button点击事件
     * @param v
     */
    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_selectAll:    //全选
                for (int i = 0;i<mList.size();i++){
                    mList.get(i).setSelect(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_delect:   //删除

                delectTask();
                break;
            case R.id.btn_cancel:   //取消
                cancelTask();
                break;
        }
    }

    /**
     * 删除任务的方法
     */
    private void delectTask(){
        List<DataBean> data = new ArrayList<>();
        for(DataBean bean : mList){
            if(bean.isSelect()){
                data.add(bean);
            }
        }
        for(int i = 0;i<data.size();i++){
                delete(data.get(i).getId());
                mList.remove(data.get(i));
        }
        if(data.size()!=0){
            LogUtils.showToast("任务已删除成功");
        }
        cancelTask();
    }
    /**
     * 删除方法
     * @param id
     */
    private void delete(int id) {
        String sql = "delete from taskinfo where id=?";
        db.execSQL(sql, new Object[]{id});
    }
    /**
     * 取消选择
     */
    private void cancelTask(){
        if(isLineaLayoutVisible){
            bottom_layout.setVisibility(View.GONE);
            isLineaLayoutVisible = false;
            for(DataBean bean : mList){
                bean.setSelect(false);
                bean.setShowCheckBox(false);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ListView长按点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        mList.get(position).isSelect();// 记录选择的item
        for(DataBean bean :mList){
            bean.setShowCheckBox(true);// 将所有的Item的CheckBox设置为选择状态
        }
        mAdapter.notifyDataSetChanged();
        bottom_layout.setVisibility(View.VISIBLE);
        isLineaLayoutVisible = true;
        return true;
    }


    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(isLineaLayoutVisible){
            // 记录被选择的item
            mList.get(position).setSelect(!mList.get(position).isSelect());
            mAdapter.notifyDataSetChanged();
        }
    }
}
