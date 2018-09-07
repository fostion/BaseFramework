package cn.base.demo.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import cm.base.framework.base.BaseActivity;
import cm.base.framework.base.BasePresenter;
import cm.base.framework.utils.SignUtil;
import cn.base.demo.R;

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    private LoginPresenter loginPresenter;
    private Button btnStart, btnStop;
    private Button btnOpen,btnIDOpen,btnNumOpen,btnNameOpen,btnPre,btnNext;

    @Nullable
    @Override
    public BasePresenter bindPresenter() {
        loginPresenter = new LoginPresenter(this);
        return loginPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.btnStart);
        btnStop = (Button)findViewById(R.id.btnStop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test_log", "sign1: "+ SignUtil.getAppSignature(getBaseContext()));
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test_log", "sign2: "+ SignUtil.getAppSignature(getBaseContext()));
            }
        });

        btnOpen = (Button) findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.mylove.galaxy.APP_OPEN");
                LoginActivity.this.startActivity(intent);
            }
        });

        btnIDOpen = (Button) findViewById(R.id.btnId);
        btnIDOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.mylove.galaxy.APP_OPEN");
                intent.putExtra("id", "ff808081382bf0b301382bf1853e019d");
                LoginActivity.this.startActivity(intent);
            }
        });

        btnNameOpen = (Button) findViewById(R.id.btnName);
        btnNameOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.mylove.galaxy.APP_OPEN");
                intent.putExtra("name", "湖南卫视");

                LoginActivity.this.startActivity(intent);
            }
        });

        btnNumOpen = (Button) findViewById(R.id.btnNum);
        btnNumOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.mylove.galaxy.APP_OPEN");
                intent.setPackage("com.mylove.galaxy");
                intent.putExtra("number", 88);
                intent.putExtra("from", "com.myapp.demo");
                LoginActivity.this.startActivity(intent);
            }
        });

        btnPre = (Button) findViewById(R.id.btnPre);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.mylove.galaxy.CHANNEL_PREV_CHANNEL");
                LoginActivity.this.startActivity(intent);
            }
        });

        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.mylove.galaxy.CHANNEL_NEXT_CHANNEL");
                LoginActivity.this.startActivity(intent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean startRxBus() {
        return true;
    }

    @Override
    public void loginSucc() {

    }

    @Override
    public void loginFail() {
    }
}
