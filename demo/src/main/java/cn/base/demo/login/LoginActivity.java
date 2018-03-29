package cn.base.demo.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cm.base.framework.base.BaseActivity;
import cm.base.framework.base.BasePresenter;
import cm.base.framework.utils.L;
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

        int i = 10;
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
