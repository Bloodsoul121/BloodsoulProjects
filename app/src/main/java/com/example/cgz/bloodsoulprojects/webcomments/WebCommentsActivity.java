package com.example.cgz.bloodsoulprojects.webcomments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.cgz.bloodsoulprojects.R;

import java.util.ArrayList;
import java.util.List;

public class WebCommentsActivity extends Activity {

    private CustomScrollView mScrollView;
    private CustomWebview mWebView;
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private WebCommentsActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_comments);

        mScrollView = (CustomScrollView) findViewById(R.id.customscrollview);
        mWebView = (CustomWebview) findViewById(R.id.webview);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mContext = this;

        initWebview();
        initData();
        initRecyclerView();
    }

    private void initWebview() {
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://m.happyjuzi.com/article/168934.html?view=27&pf=android&d_s" +
                "ource=toutiao&&from=toutiao&app_name=open_news&app_id=34&utm_campaign=open&ut" +
                "m_medium=webview&utm_source=jinlillq");
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
