package cm.base.framework.utils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import cm.base.framework.service.Remote;
import cm.base.framework.service.exception.ApiException;
import rx.Observable;
import rx.functions.Func1;

/**
 * 错误重试
 * Created by fostion on 2018/3/29.
 */

public class RetryWithDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetry;
    private int retryCount = 0;

    public RetryWithDelay(){
        maxRetry = 3; //默认使用3次重试
    }

    public RetryWithDelay(int maxRetry){
        this.maxRetry = maxRetry;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        //这个数据是由底层封装的，若是有失败
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (isRetryException(throwable) && ++retryCount <= maxRetry){
                    //todo 重试, 这里需要判断错误，网络异常,SocketTimeOut,那些错误才重试
                    //定时发送，使用二次数，延时
                    long retryTimeSecond = (long)Math.pow(2, retryCount);
                    L.e(Remote.TAG, "request error and now retry:"+retryCount + " and it will retry after "+ retryTimeSecond +" seconds");
                    L.e(Remote.TAG, throwable.getLocalizedMessage());
                    return Observable.timer(retryTimeSecond, TimeUnit.SECONDS);
                }

                //否则直接回调错误
                return Observable.error(throwable);
            }
        });
    }

    //部分exception, 不重试
    private boolean isRetryException(Throwable throwable){
        if(throwable == null){
            return false;
        } else if(throwable instanceof SocketTimeoutException || throwable instanceof UnknownHostException){
            return true;
        } else if(throwable instanceof ApiException){
            //todo 根据项目具体状态码，选择重试
            return false;
        }
        return false;
    }
}
