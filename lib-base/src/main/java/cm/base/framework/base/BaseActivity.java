package cm.base.framework.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hwangjr.rxbus.RxBus;

public abstract class BaseActivity  extends AppCompatActivity{

    protected BasePresenter mPresenter;

    //绑定服务
    @Nullable
    public abstract BasePresenter bindPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = bindPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (startRxBus()){
            registerBus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void registerBus(){
        RxBus.get().register(this);
    }

    protected void unregisterBus(){
        RxBus.get().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null){
            mPresenter.onStop();
        }
        if (startRxBus()){
            unregisterBus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestory();
        }
    }

    //是否使用rxbus
    public boolean startRxBus(){
        return false;
    }
}
