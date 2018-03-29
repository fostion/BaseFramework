package cm.base.framework.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;
import cm.base.framework.base.BaseApplication;
import cm.base.framework.service.api.UserApi;
import cm.base.framework.service.compont.LoggingInterceptor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络远程请求
 */
public class Remote {

    public static final String TAG = "NetworkReqeust";
    private static final String BASE_URL = "http://www.weather.com.cn/data/";
    private static final String cacheFileName = "cache_response";
    private static final long RESPONSE_CACHE_SIZE = 10 * 1024 * 1024L;
    private static final long HTTP_CONNECT_TIMEOUT = 10L;
    private static final long HTTP_READ_TIMEOUT = 30L;
    private static final long HTTP_WRITE_TIMEOUT = 10L;

    private static Retrofit adapter;

    private static Retrofit getAdapter() {
        if (adapter == null) {
            synchronized (Remote.class) {
                if (adapter == null) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create();
                    LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
                    loggingInterceptor.setLevel(LoggingInterceptor.Level.ALL);

                    //config OkHttp
                    OkHttpClient okHttpClient
                            = new OkHttpClient.Builder()
                            .cache(new Cache(
                                    new File(BaseApplication.INSTANCE.getApplicationContext().getCacheDir(), cacheFileName)
                                    , RESPONSE_CACHE_SIZE))
                            .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                            .addInterceptor(loggingInterceptor)//设置日志打印
                            .build();

                    //create retrofit
                    adapter = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(okHttpClient)
                            .baseUrl(BASE_URL)
                            .build();
                }
            }
        }
        return adapter;
    }

    //userApi
    public static UserApi userApi = getAdapter().create(UserApi.class);
}
