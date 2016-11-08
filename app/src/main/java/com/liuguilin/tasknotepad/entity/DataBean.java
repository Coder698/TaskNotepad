package com.liuguilin.tasknotepad.entity;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.entity
 *  文件名:   DataBean
 *  创建者:   LGL
 *  创建时间:  2016/10/24 15:32
 *  描述：    数据实体
 */

import java.io.Serializable;

public class DataBean implements Serializable {

    private Integer id;
    private String day;
    //时间
    private String time;
    //信息
    private String msg;
    private boolean isSelect;
    private boolean isShowCheckBox;

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getDay() {
        return day;
    }


    public void setDay(String day) {
        this.day = day;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public boolean isSelect() {
        return isSelect;
    }


    public void setSelect(boolean select) {
        isSelect = select;
    }


    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }


    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
    }
}
