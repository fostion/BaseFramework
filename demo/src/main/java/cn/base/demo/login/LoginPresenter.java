package cn.base.demo.login;

/**
 * login P 处理逻辑
 */
class LoginPresenter implements LoginContract.ILoginPresenter {

    private LoginContract.ILoginView view;

    public LoginPresenter(LoginContract.ILoginView view){
        this.view = view;
    }

    @Override
    public void login() {
    }
}
