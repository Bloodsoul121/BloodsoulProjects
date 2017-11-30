package com.example.cgz.bloodsoulprojects;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    protected void llog(String msg) {
        llog(getClass().getSimpleName(), msg);
    }

    protected void llog(String tag, String msg) {
        Log.i(tag, " ---> " + msg);
    }

}
