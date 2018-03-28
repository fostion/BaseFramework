package cn.base.demo.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import cm.base.framework.base.BaseActivity;
import cm.base.framework.base.BasePresenter;
import cn.base.demo.R;

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    private LoginPresenter loginPresenter;

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

        //调用p层逻辑
        loginPresenter.login();
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
