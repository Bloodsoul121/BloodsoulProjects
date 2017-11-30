package com.example.cgz.bloodsoulprojects;

import android.app.Application;
import android.util.Log;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO 集成：1.4、初始化数据服务SDK、初始化设备信息并启动推送服务
        // 初始化BmobSDK
        Bmob.initialize(this, "f67416d583109e97523cedc3139895dc");
        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    log(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                } else {
                    log(e.getMessage());
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(this);
    }

    private void log(String msg) {
        Log.i("BaseActivity", " ---> " + msg);
    }

}
