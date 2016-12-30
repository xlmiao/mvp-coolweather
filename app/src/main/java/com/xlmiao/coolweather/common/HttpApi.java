package com.xlmiao.coolweather.common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XuLeMiao on 2016/12/21.
 */

public class HttpApi {

    private static IHttpService ERVICE;

    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10000;

    public  static IHttpService getDefaultApi(){
        if(null == ERVICE){
            /**
             * OkHttp3 的应用 可加载 头 固定参数 Interceptor cookie等
             */
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            okHttpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl httpUrl = request.url().newBuilder()
                            .addQueryParameter("","")
                            .build();

                    request.newBuilder() //可查看 发送的httpUrl详细信息，可以看Https
                            .url(httpUrl)
                            .build();

                    return chain.proceed(request);
                }
            });

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor).build();

            ERVICE = new Retrofit.Builder()
                    .client(okHttpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(IHttpService.API_SERVER_URL)
                    .build()
                    .create(IHttpService.class);
        }
        return ERVICE;
    }
}
