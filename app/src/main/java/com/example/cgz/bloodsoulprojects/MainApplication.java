package com.example.cgz.bloodsoulprojects;

import android.app.Application;
import android.util.Log;

import com.example.cgz.bloodsoulprojects.bmob.im.DemoMessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
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
        initBmob();
        // 使用推送服务时的初始化操作
        initBmobPush();
        //TODO 集成：1.8、初始化IM SDK，并注册消息接收器
        initBmobIM();
    }

    private void initBmobIM() {
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler());
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initBmobPush() {
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

    private void initBmob() {
        Bmob.initialize(this, "f67416d583109e97523cedc3139895dc");
    }

    private void log(String msg) {
        Log.i("BaseActivity", " ---> " + msg);
    }

}
