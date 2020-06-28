package com.xinkao.skmvp.network.download;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.reactivex.Observer;
import okhttp3.Interceptor;
import okhttp3.Response;

public class DownloadInterceptor implements Interceptor {

    Observer<Integer> observer;

    public DownloadInterceptor(Observer<Integer> observer) {
        this.observer = observer;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(new DownloadResponseBody(observer, response.body())).build();
    }
}
