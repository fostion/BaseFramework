package cn.base.demo.login;

import cm.base.framework.base.BaseView;

/**
 * Created by fostion on 2016/12/30.
 */

public interface LoginContract {

    interface ILoginView extends BaseView{
        void loginSucc();
        void loginFail();
    }

    interface ILoginPresenter extends BaseView{
        void login();
    }

}
