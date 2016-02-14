package com.example.gulshngill.flickrbrowser;

/**
 * Created by gulshngill on 15/02/2016.
 */
enum DownloadStaus {IDLE, PROCESSING, NOT_INITIALISED,FAILED_OR_EMPTY, OK }


public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStaus mDownloadStatus;

    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }
}
