package cm.base.framework.service;

import cm.base.framework.service.api.bean.Response;
import cm.base.framework.service.api.bean.Weather;
import cm.base.framework.service.exception.ApiCode;
import cm.base.framework.service.exception.ApiException;
import cm.base.framework.utils.RxFunction;
import rx.Observable;
import rx.functions.Func1;

/**
 * 获取数据服务
 * Created by fostion on 2018/3/28.
 */

public class UserService {

    public static Observable<Weather> login() {
        //对执行结果处理
        return Remote.userApi.login()
                .flatMap(new Func1<Response<Weather>, Observable<Weather>>() {
                    @Override
                    public rx.Observable<Weather> call(Response<Weather> weatherResponse) {
                        if (weatherResponse == null) {
                            throw new NullPointerException("response noting");
                        }
                        //非200状态码，直接抛出异常
                        int code = weatherResponse.getCode();
                        if (code == ApiCode.OK && weatherResponse.getData() != null) {
                            return Observable.just(weatherResponse.getData());
                        } else {
                            String msg = weatherResponse.getMsg();
                            throw new ApiException(code, msg);
                        }
                    }
                });
    }
}
