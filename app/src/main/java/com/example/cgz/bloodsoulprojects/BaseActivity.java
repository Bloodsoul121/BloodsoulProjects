package com.example.cgz.bloodsoulprojects;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    protected void llog(String msg) {
        llog("BaseActivity", msg);
    }

    protected void llog(String tag, String msg) {
        Log.i(tag, " ---> " + msg);
    }

}
