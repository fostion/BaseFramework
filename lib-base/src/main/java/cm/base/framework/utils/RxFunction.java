package cm.base.framework.utils;

import cm.base.framework.service.api.bean.Response;
import cm.base.framework.service.exception.ApiCode;
import cm.base.framework.service.exception.ApiException;
import rx.Observable;
import rx.functions.Func1;

/**
 * rx公共方法
 * Created by fostion on 2018/3/28.
 */

public class RxFunction {

    //减少层级,拆封数据，抛出异常
    public static <T> Func1<Response<T>, Observable<T>> unbox(){
        return new Func1<Response<T>, Observable<T>>() {
            @Override
            public rx.Observable<T> call(Response<T> weatherResponse) {
                if (weatherResponse == null){
                    return Observable.error(new NullPointerException("response noting"));
                }
                //非200状态码，直接抛出异常
                int code = weatherResponse.getCode();
                if (code == ApiCode.OK && weatherResponse.getData() != null){
                    return Observable.just(weatherResponse.getData());
                } else {
                    String msg = weatherResponse.getMsg();
                    return Observable.error(new ApiException(code, msg));
                }
            }
        };
    }
}
