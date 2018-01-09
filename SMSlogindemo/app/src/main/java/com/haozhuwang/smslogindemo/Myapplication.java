package com.haozhuwang.smslogindemo;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2018/1/9
 */

public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
