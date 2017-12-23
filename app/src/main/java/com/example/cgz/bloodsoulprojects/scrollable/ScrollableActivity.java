package com.example.cgz.bloodsoulprojects.scrollable;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cgz.bloodsoulprojects.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollableActivity extends AppCompatActivity implements ScrollableHelper.ScrollableContainer {

    private ScrollableLayout mScrollLayout;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable);
        mContext = this;
        mScrollLayout = (ScrollableLayout) findViewById(R.id.scrollableLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mScrollLayout.getHelper().setCurrentScrollableContainer(this);
        initData();
        initRecyclerView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            mDatas.add("item - " + i);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        Adapter adapter = new Adapter();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(mContext);
            textView.setPadding(10,10,10,10);
            return new Holder(textView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            TextView tv = (TextView) holder.mView;
            tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            public View mView;

            public Holder(View itemView) {
                super(itemView);
                mView = itemView;
            }
        }
    }
}
