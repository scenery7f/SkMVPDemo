package com.xinkao.skmvp.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static String BASE_URL;
    private static boolean LOGGER = false;

    private Retrofit retrofit;

    public static RetrofitManager instance;

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient(false))
                .build();
    }

    public static RetrofitManager getInstance() {
        if (null == instance) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public static <T> T create(Class<T> service) {
        return getInstance().retrofit.create(service);
    }

    /**
     * 创建一个新的Retrofit
     *
     * @param url
     * @return
     */

    public static Retrofit newRetrofit(@NonNull String url) {
        return newRetrofit(url, false);
    }

    public static Retrofit newRetrofit(@NonNull String url, boolean isDownload) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getInstance().getOkHttpClient(isDownload))
                .build();
    }

    private OkHttpClient getOkHttpClient(boolean isDownload) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //设置统一的请求头部参数
        builder.addInterceptor(getHttpHeaderInterceptor());

        if (LOGGER && !isDownload) {
            //设置统一的添加参数的拦截器
            builder.addInterceptor(getHttpParamsInterceptor());
        }
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    /**
     * 获取HTTP 添加header的拦截器
     *
     * @return
     */
    private Interceptor getHttpHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("User-Agent", "Android")
                        .method(request.method(), request.body())
                        .build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 获取HTTP 添加公共参数的拦截器
     * 暂时支持get、head请求&Post put patch的表单数据请求
     *
     * @return
     */
    private Interceptor getHttpParamsInterceptor() {
        return new LogInterceptor();

//        return new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                if (request.method().equalsIgnoreCase("GET") || request.method().equalsIgnoreCase("HEAD")) {
//                    HttpUrl httpUrl = request.url().newBuilder()
//                            .addQueryParameter("version", "1.1.0")
//                            .addQueryParameter("devices", "android")
//                            .build();
//                    request = request.newBuilder().url(httpUrl).build();
//                } else {
//                    RequestBody originalBody = request.body();
//                    if (originalBody instanceof FormBody) {
//                        FormBody.Builder builder = new FormBody.Builder();
//                        FormBody formBody = (FormBody) originalBody;
//                        for (int i = 0; i < formBody.size(); i++) {
//                            builder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
//                        }
//                        FormBody newFormBody = builder.addEncoded("version", "1.1.0")
//                                .addEncoded("devices", "android")
//                                .build();
//                        if (request.method().equalsIgnoreCase("POST")) {
//                            request = request.newBuilder().post(newFormBody).build();
//                        } else if (request.method().equalsIgnoreCase("PATCH")) {
//                            request = request.newBuilder().patch(newFormBody).build();
//                        } else if (request.method().equalsIgnoreCase("PUT")) {
//                            request = request.newBuilder().put(newFormBody).build();
//                        }
//
//                    } else if (originalBody instanceof MultipartBody) {
//
//                    }
//
//                }
//                return chain.proceed(request);
//            }
//        };
    }

    /**
     * 首次调用NetWorManager前使用
     *
     * @param url
     */
    public static void config(@NonNull String url) {
        BASE_URL = url;
    }

    public static void config(boolean LOGGER) {
        RetrofitManager.LOGGER = LOGGER;
    }

    public static void config(@NonNull String url, boolean logger) {
        BASE_URL = url;
        LOGGER = logger;
    }
}
