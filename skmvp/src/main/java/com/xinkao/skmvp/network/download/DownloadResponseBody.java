package com.xinkao.skmvp.network.download;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import io.reactivex.Observer;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadResponseBody extends ResponseBody {

    ResponseBody responseBody;
    Observer<Integer> observer;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(Observer<Integer> observer, ResponseBody responseBody) {
        this.responseBody = responseBody;
        this.observer = observer;
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @NotNull
    @Override
    public BufferedSource source() {
        if (bufferedSource == null)
            bufferedSource = Okio.buffer(source(responseBody.source()));

        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);

                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (observer != null) {
                    if (bytesRead != -1) {
                        observer.onNext((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }
                }

                return bytesRead;
            }
        };
    }
}
