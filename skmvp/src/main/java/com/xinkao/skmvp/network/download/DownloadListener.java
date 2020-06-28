package com.xinkao.skmvp.network.download;

public interface DownloadListener {
    void onProgress(int progress);

    void onError(Throwable error);

    void onSuccess(String path);

    void onCancel();
}
