package com.example.cgz.bloodsoulprojects.bmob;

import android.os.Bundle;
import android.view.View;

import com.example.cgz.bloodsoulprojects.BaseActivity;
import com.example.cgz.bloodsoulprojects.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BmobActivity extends BaseActivity {

    private static final String TAG = "Bmob ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmob);
        initBmob();
    }

    private void initBmob() {
        //第一：默认初始化
        Bmob.initialize(this, "f67416d583109e97523cedc3139895dc");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

    /**
     * 添加一行数据
     */
    public void clickBtn1(View view) {
        BmobBean bmobBean = new BmobBean();
        bmobBean.setId("1");
        bmobBean.setName("xxx");
        bmobBean.setAddress("diqiu");
        bmobBean.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e == null){
                    llog(TAG + "添加数据成功，返回objectId为：" + objectId);
                } else {
                    llog(TAG + "创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    public void clickBtn2(View view) {
        BmobQuery<BmobBean> query = new BmobQuery<>();
        query.getObject("dbd017cc05", new QueryListener<BmobBean>() {
            @Override
            public void done(BmobBean bmobBean, BmobException e) {
                if(bmobBean != null){
                    llog(TAG + bmobBean.toString());
                }else{
                    llog(TAG + e.getMessage());
                }
            }
        });
    }

    public void clickBtn3(View view) {
        BmobBean bmobBean = new BmobBean();
        bmobBean.setAddress("huoxing");
        bmobBean.update("dbd017cc05", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    llog(TAG + "更新成功");
                }
            }
        });
    }

    public void clickBtn4(View view) {
        BmobBean bmobBean = new BmobBean();
        bmobBean.delete("dbd017cc05", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    llog(TAG + "删除成功");
                }
            }
        });
    }

    public void clickBtn5(View view) {
        List<BmobObject> persons = new ArrayList<>();
        BmobBean p1 = new BmobBean();
        p1.setName("aaa");
        BmobBean p2 = new BmobBean();
        p2.setName("bbb");
        BmobBean p3 = new BmobBean();
        p3.setName("ccc");

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);

        new BmobBatch().insertBatch(persons).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i=0;i<o.size();i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            llog(TAG, "第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                        }else{
                            llog(TAG, "第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                }else{
                    llog(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void clickBtn6(View view) {
        List<BmobObject> persons = new ArrayList<>();
        BmobBean p1 = new BmobBean();
        p1.setObjectId("7b6febb04a");
        p1.setId("22");
        BmobBean p2 = new BmobBean();
        p2.setObjectId("0b39b5c4a3");
        p2.setId("23");
        BmobBean p3 = new BmobBean();
        p3.setObjectId("6e95009f42");
        p3.setId("24");

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);

        new BmobBatch().updateBatch(persons).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i=0;i<o.size();i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            llog(TAG, "第"+i+"个数据批量更新成功："+result.getUpdatedAt());
                        }else{
                            llog(TAG, "第"+i+"个数据批量更新失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                }else{
                    llog(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


    /**
     * 批量操作每次只支持最大50条记录的操作。
     * 批量操作不支持对User表的操作。
     */
    public void clickBtn7(View view) {
        List<BmobObject> persons = new ArrayList<>();
        BmobBean p1 = new BmobBean();
        p1.setObjectId("7b6febb04a");
        BmobBean p2 = new BmobBean();
        p2.setObjectId("0b39b5c4a3");
        BmobBean p3 = new BmobBean();
        p3.setObjectId("6e95009f42");

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);

        new BmobBatch().deleteBatch(persons).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i=0;i<o.size();i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            llog(TAG, "第"+i+"个数据批量删除成功");
                        }else{
                            llog(TAG, "第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                }else{
                    llog(TAG, "失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    /**
     * 查询某个数据表中的所有数据是非常简单的查询操作，查询的数据条数最多500.
     * 例如：查询GameScore表中playerName为“比目”的50条数据记录。
     */
    public void clickBtn8(View view) {
        BmobQuery<BmobBean> query = new BmobQuery<>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("name", "ccc");
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //BmobBean
        query.findObjects(new FindListener<BmobBean>() {
            @Override
            public void done(List<BmobBean> object, BmobException e) {
                if(e==null){
                    llog(TAG, "查询成功：共"+object.size()+"条数据。");
                    for (BmobBean gameScore : object) {
                        //获得playerName的信息
                        gameScore.getName();
                        //获得数据的objectId信息
                        gameScore.getObjectId();
                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                        gameScore.getCreatedAt();
                    }
                }else{
                    llog(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

}
