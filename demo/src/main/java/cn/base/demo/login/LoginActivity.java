package cn.base.demo.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import cm.base.framework.base.BaseActivity;

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView {

    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //p层与view绑定
        loginPresenter = new LoginPresenter(this);

        //调用p层逻辑
        loginPresenter.login();
    }

    @Override
    public void loginSucc() {

    }

    @Override
    public void loginFail() {

    }
}
