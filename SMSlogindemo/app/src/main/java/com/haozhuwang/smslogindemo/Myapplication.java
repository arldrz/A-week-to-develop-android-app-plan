package com.haozhuwang.smslogindemo;

import com.mob.MobApplication;
import com.mob.MobSDK;

import org.xutils.x;

/**
 * Created by Administrator on 2018/1/9
 */

public class Myapplication extends MobApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);

        MobSDK.init(this);
    }
}
