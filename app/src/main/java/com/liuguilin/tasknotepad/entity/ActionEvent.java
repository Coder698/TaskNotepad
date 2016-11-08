package com.liuguilin.tasknotepad.entity;

import java.util.List;

/**
 * Created by adu on 2016/10/26.
 */

public class ActionEvent {
    private String Type;
    private List<DataBean> list;


    public String getType() {
        return Type;
    }


    public void setType(String type) {
        Type = type;
    }


    public List<DataBean> getList() {
        return list;
    }


    public void setList(List<DataBean> list) {
        this.list = list;
    }
}
