package com.xinkao.skmvp.network.download;

import android.os.Handler;
import android.os.Message;

import com.xinkao.skmvp.utils.FileUtils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownloadFile {

    DownloadListener listener;
    Observer<Integer> observer;
    String filePath;
    Disposable disposable;
    final int DEFAULT_TIMEOUT = 15;

    Retrofit retrofit;

    ProgressHandler handler;

    public DownloadFile() {
    }

    public DownloadFile setListener(DownloadListener listener) {
        this.listener = listener;
        handler = new ProgressHandler(listener);
        return this;
    }

    public DownloadFile build(URL url) {
        return build(url.getProtocol() + "://" + url.getAuthority());
    }

    public DownloadFile build(String baseUrl) {
        observer = getObserver();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return this;
    }

    /**
     * 开始下载
     *
     * @param url
     * @param filePath
     */
    public void download(String url, String filePath) {
        this.filePath = filePath;

        retrofit.create(DownloadFileModel.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody downloadResponseBody) throws Exception {
                        return downloadResponseBody.byteStream();
                    }
                })
                .map(new Function<InputStream, Integer>() {
                    @Override
                    public Integer apply(InputStream inputStream) throws Exception {
                        Exception e = FileUtils.writeFile(inputStream, filePath);
                        if (e == null) {
                            return 100;
                        } else {
                            observer.onError(e);
                            return 0;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 取消加载
     */
    public void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            observer.onError(null);
            disposable.dispose();
        }
    }

    private Observer<Integer> getObserver() {
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer p) {
                if (handler != null) {
//                    listener.onProgress(p);
                    Message msg = handler.obtainMessage();
                    msg.what = 3;
                    msg.arg1 = p;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Throwable e) {

                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    if (e == null) {
//                        listener.onCancel();
                        msg.what = 2;

                    } else {
//                        listener.onError(e);
                        msg.what = 0;
                        msg.obj = e;
                    }
                    handler.sendMessage(msg);
                }

                if (e != null) {
                    e.printStackTrace();
                }
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                if (handler != null) {
//                    listener.onSuccess(filePath);
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = filePath;
                    handler.sendMessage(msg);
                }
            }
        };
    }

    private OkHttpClient getClient() {

        DownloadInterceptor interceptor = new DownloadInterceptor(observer);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class ProgressHandler extends Handler {
        WeakReference<DownloadListener> wrListener;

        public ProgressHandler(DownloadListener wrListener) {
            this.wrListener = new WeakReference<>(wrListener);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0: // 失败
                    wrListener.get().onError((Throwable) msg.obj);
                    break;
                case 1: // 成功
                    wrListener.get().onSuccess(msg.obj.toString());
                    break;
                case 2: // 取消
                    wrListener.get().onCancel();
                    break;
                case 3: // 进度
                    wrListener.get().onProgress(msg.arg1);
            }
        }
    }
}
