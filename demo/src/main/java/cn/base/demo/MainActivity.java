package cn.base.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.base.demo.greendaodemo.GreenDaoActivity;
import cn.base.demo.login.LoginActivity;
import cn.base.demo.tvrecyclerview.DemoActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DemoActivity.class);
        startActivity(intent);
        finish();
    }
}
