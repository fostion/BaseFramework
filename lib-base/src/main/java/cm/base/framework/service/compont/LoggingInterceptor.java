package cm.base.framework.service.compont;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Set;

import cm.base.framework.service.api.bean.Weather;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 日志拦截器
 * Created by fostion on 2018/3/28.
 */

public class LoggingInterceptor implements Interceptor {

    private final String TAG = "LoggingInterceptor";
    private Level level;

    public void setLevel(Level level){
        this.level = level;
    }

    public void log(String msg){
        Log.i(TAG, msg);
    }

    public enum Level{
        NONE, ALL
    }


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        //请求链接
        Headers headers  = request.headers(); //header
        log("------------------------------request----------------------------------");
        Set<String> nameSets = headers.names();
        for (String name:nameSets){
            log(name+":"+headers.get(name));
        }
        log("method: "+request.method());
        log("url: "+request.url());
        log("-----------------------------------------------------------------------");

        //若是出现异常，将不会执行先main代码
        Response response = chain.proceed(request);

        //返回结果
        int code = response.code();
        ResponseBody responseBody = response.body();
        MediaType mediaType = responseBody.contentType();
        String bodyContentStr = responseBody.string();
        log("-----------------------------response-----------------------------------");
        log("code:"+code);
        log("url:"+response.request().url());
        log("content:"+bodyContentStr);
        log("------------------------------------------------------------------------");

        //todo remove
        cm.base.framework.service.api.bean.Response tmpResponse = new cm.base.framework.service.api.bean.Response<>();
        tmpResponse.setCode(403);
        tmpResponse.setMsg("request success");
        tmpResponse.setData(new Gson().fromJson(bodyContentStr, Weather.class));
        String gsonStr = new Gson().toJson(tmpResponse);

        log("tmp result: "+gsonStr);


        //okhttp string方法只能读取一次，需要重新构建
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, gsonStr))
                .build();
    }
}
