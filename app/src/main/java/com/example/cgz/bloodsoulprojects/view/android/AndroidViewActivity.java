package com.example.cgz.bloodsoulprojects.view.android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cgz.bloodsoulprojects.BaseActivity;
import com.example.cgz.bloodsoulprojects.R;

import java.util.ArrayList;
import java.util.List;

public class AndroidViewActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;

    private List<String> mDatas = new ArrayList<>();

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_view);

        initData();
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void initData() {
        mDatas.add("SwipeRefreshLayout");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(SwipeRefreshLayoutActivity.class, null);
                break;
        }
    }
}
