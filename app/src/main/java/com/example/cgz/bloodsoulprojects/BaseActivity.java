package com.example.cgz.bloodsoulprojects;

import android.app.Activity;
import android.content.Intent;
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

    public void startActivity(Class<? extends Activity> target) {
        startActivity(target, null);
    }

    /**启动指定Activity
     * @param target
     * @param bundle
     */
    public void startActivity(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(this.getPackageName(), bundle);
        startActivity(intent);
    }

}
