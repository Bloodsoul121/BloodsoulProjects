package com.example.cgz.bloodsoulprojects.view;

import android.os.Bundle;
import android.view.View;

import com.example.cgz.bloodsoulprojects.BaseActivity;
import com.example.cgz.bloodsoulprojects.R;
import com.example.cgz.bloodsoulprojects.view.android.AndroidViewActivity;

public class ViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
    }

    public void onClick1(View view) {
        startActivity(AndroidViewActivity.class);
    }

    public void onClick2(View view) {

    }
}
