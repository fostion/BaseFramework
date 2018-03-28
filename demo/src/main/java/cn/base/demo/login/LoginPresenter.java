package cn.base.demo.login;

import android.util.Log;
import cm.base.framework.base.BasePresenter;
import cm.base.framework.service.UserService;
import cm.base.framework.service.api.bean.Weather;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 * login P 处理逻辑
 */
class LoginPresenter extends BasePresenter implements LoginContract.ILoginPresenter {

    private LoginContract.ILoginView view;

    public LoginPresenter(LoginContract.ILoginView view) {
        this.view = view;
    }

    @Override
    public void login() {
        UserService.login()
                .compose(this.<Weather>bindLife())
                .subscribeOn(Schedulers.io())
                .observeOn(onUI())
                .subscribe(new Action1<Weather>() {
                    @Override
                    public void call(Weather s) {
                        Log.i("test_log", "结果："+s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("test_log", "异常:" + throwable.toString());
                        throwable.printStackTrace();
                    }
                });

    }

    void checkLifeStyle(){
        Observable.interval(3, TimeUnit.SECONDS)
                .compose(this.<Long>bindLife())
                .observeOn(onUI())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //结果处理
                        Log.i("test_log", "执行中...");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //todo 错误处理
                        Log.i("test_log", "异常:" + throwable.toString());
                    }
                });
    }
}
