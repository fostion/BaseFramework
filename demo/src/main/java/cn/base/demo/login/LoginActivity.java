package cn.base.demo.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ipmacro.ppcore.BaseDownload;
import com.ipmacro.ppcore.M3U8Download;
import com.ipmacro.ppcore.PPCore;
import cm.base.framework.base.BaseActivity;
import cm.base.framework.base.BasePresenter;
import cm.base.framework.utils.ReflectUtil;
import cm.base.framework.utils.SignUtil;
import cn.base.demo.HookPackageManager;
import cn.base.demo.R;

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    private LoginPresenter loginPresenter;
    private Button btnStart, btnStop;

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
                HookPackageManager.startHook(LoginActivity.this);
                Log.i("test_log", "sign1: "+ SignUtil.getAppSignature(getBaseContext()));
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HookPackageManager.stopHook();
                Log.i("test_log", "sign2: "+ SignUtil.getAppSignature(getBaseContext()));
            }
        });

        //调用p层逻辑
//        loginPresenter.login();

        HookPackageManager.startHook(this);
        PPCore.init(this);
        PPCore.login4(getBaseContext(), 0);
        final BaseDownload download = new M3U8Download();

        String playUrl = "http://125.88.70.140:30001/PLTV/88888956/224/3221227692/2.m3u8";
//        String playUrl = "https://videopull.10jqka.com.cn:8188/diwukejibenmianxuangufangfa_1505989287.flv";
        download.setHeader("");
        download.setSrcUrl(playUrl);
        download.start(playUrl);

        new Thread(){
            @Override
            public void run() {
                boolean isPrepare = false;
                while (!isPrepare){
                    isPrepare = download.prepare2();
                }
                Log.i("test_log", "url:"+download.getPlayUrl());

                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

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
