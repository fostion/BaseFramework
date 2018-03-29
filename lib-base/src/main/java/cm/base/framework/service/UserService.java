package cm.base.framework.service;

import cm.base.framework.service.api.bean.Weather;
import cm.base.framework.utils.RetryWithDelay;
import cm.base.framework.utils.RxFunction;
import rx.Observable;

/**
 * 获取数据接口服务
 * Created by fostion on 2018/3/28.
 */

public class UserService {

    public static Observable<Weather> login() {
        //数据是经过封装的，需要将数据拆出来
        return Remote.userApi.login()
                .flatMap(RxFunction.<Weather>unbox())
                .retryWhen(new RetryWithDelay());
    }
}
