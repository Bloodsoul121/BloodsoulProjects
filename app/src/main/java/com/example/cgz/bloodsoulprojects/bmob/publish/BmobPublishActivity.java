package com.example.cgz.bloodsoulprojects.bmob.publish;

import android.os.Bundle;
import android.view.View;

import com.example.cgz.bloodsoulprojects.BaseActivity;
import com.example.cgz.bloodsoulprojects.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import rx.functions.Action1;

public class BmobPublishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmob_publish);
    }

    public void clickBtn1(View view) {
        BmobPushManager bmobPushManager = new BmobPushManager();
        bmobPushManager.pushMessageAll("消息内容", new PushListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    toast("推送成功！");
                }else {
                    llog("BmobPublishActivity", "异常：" + e.getMessage());
                }
            }
        });
    }

    public void clickBtn2(View view) {
        BmobPushManager bmobPushManager = new BmobPushManager();
        BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
        List<String> channels = new ArrayList<>();
        //TODO 替换成你需要推送的所有频道，推送前请确认已有设备订阅了该频道，也就是channels属性存在该值
        channels.add("NBA");
        query.addWhereContainedIn("channels", channels);
        bmobPushManager.setQuery(query);
        bmobPushManager.pushMessage("消息内容", new PushListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast("推送成功！");
                } else {
                    llog("BmobPublishActivity", "异常：" + e.getMessage());
                }
            }
        });
    }

    public void clickBtn3(View view) {
        String installationId = BmobInstallationManager.getInstallationId();
        llog("BmobPublishActivity", "installationId --> " + installationId);
        BmobInstallation currentInstallation = BmobInstallationManager.getInstance().getCurrentInstallation();
        llog("currentInstallation --> " + currentInstallation.getChannels().toString());
    }

    public void clickBtn4(View view) {
        modifyInstallationUser(new User("zhangsan", 18));
    }

    /**
     * 修改设备表的用户信息：先查询设备表中的数据，再修改数据中用户信息
     * @param user
     */
    private void modifyInstallationUser(final User user) {
        BmobQuery<Installation> bmobQuery = new BmobQuery<>();
        final String id = BmobInstallationManager.getInstallationId();
        bmobQuery.addWhereEqualTo("installationId", id);
        bmobQuery.findObjectsObservable(Installation.class)
                .subscribe(new Action1<List<Installation>>() {
                    @Override
                    public void call(List<Installation> installations) {

                        if (installations.size() > 0) {
                            Installation installation = installations.get(0);
                            installation.setUser(user);
                            installation.updateObservable()
                                    .subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            toast("更新设备用户信息成功！");
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            llog("BmobPublishActivity", "更新设备用户信息失败：" + throwable.getMessage());
                                        }
                                    });

                        } else {
                            llog("BmobPublishActivity", "后台不存在此设备Id的数据，请确认此设备Id是否正确！\n" + id);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        llog("BmobPublishActivity", "查询设备数据失败：" + throwable.getMessage());
                    }
                });
    }

    public void clickBtn5(View view) {
        BmobInstallationManager.getInstance().subscribe(Arrays.asList("NBA", "CBA", "IJK", "USA"), new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    toast("批量订阅成功");
                } else {
                    llog(e.getMessage());
                }
            }
        });
    }

    public void clickBtn6(View view) {
        BmobInstallationManager.getInstance().unsubscribe(Arrays.asList("NBA", "USA"), new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    toast("批量取消订阅成功");
                } else {
                    llog(e.getMessage());
                }
            }
        });
    }
}
