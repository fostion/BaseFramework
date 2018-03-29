package cm.base.framework.service.api;

import cm.base.framework.service.api.bean.Response;
import cm.base.framework.service.api.bean.Weather;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * 用户api
 */
public interface UserApi {


    //定义网络接口
    @GET("sk/101190408.html")
    @Headers({
            "Accept: text",
            "User-Agent: demo"
    })
    Observable<Response<Weather>> login();

}
