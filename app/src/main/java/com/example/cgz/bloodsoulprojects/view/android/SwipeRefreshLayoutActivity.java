package com.example.cgz.bloodsoulprojects.view.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cgz.bloodsoulprojects.BaseActivity;
import com.example.cgz.bloodsoulprojects.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwipeRefreshLayoutActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;

    private List<String> mDatas = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh_layout);

        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mDatas.add("item - " + i);
        }
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        mListView = (ListView) findViewById(R.id.lv);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llog("OnRefreshListener - onRefresh");

                // 开始刷新，设置当前为刷新状态
                //swipeRefreshLayout.setRefreshing(true);

                // 这里是主线程
                // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
                // TODO 获取数据
                final Random random = new Random();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatas.add(0, "我是天才" + random.nextInt(100) + "号");
                        mAdapter.notifyDataSetChanged();

                        Toast.makeText(SwipeRefreshLayoutActivity.this, "刷新了一条数据", Toast.LENGTH_SHORT).show();

                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);

                // System.out.println(Thread.currentThread().getName());

                // 这个不能写在外边，不然会直接收起来
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            // 手动刷新
            case 0:
                mSwipeRefreshLayout.setRefreshing(true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
                break;
        }
    }
}
