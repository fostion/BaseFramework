package cm.base.framework.utils;

import cm.base.framework.service.api.bean.Response;
import cm.base.framework.service.api.bean.Weather;
import cm.base.framework.service.exception.ApiCode;
import cm.base.framework.service.exception.ApiException;
import rx.Observable;
import rx.functions.Func1;

/**
 * rx公共方法
 * Created by fostion on 2018/3/28.
 */

public class RxFunction {

    //请求结果对象转换
//    public static <T> Func1<Response<Weather>, Observable<T>> convertData(){
//        return new Func1<Response<Weather>, Observable<T>>() {
//            @Override
//            public rx.Observable<T> call(Response<Weather> weatherResponse) {
//                if (weatherResponse == null){
//                    throw new NullPointerException("response noting");
//                }
//                //非200状态码，直接抛出异常
//                int code = weatherResponse.getCode();
//                if (code == ApiCode.OK && weatherResponse.getData() != null){
//                    return Observable.just(weatherResponse.getData());
//                } else {
//                    String msg = weatherResponse.getMsg();
//                    throw new ApiException(code, msg);
//                }
//            }
//        };
//    }
}
