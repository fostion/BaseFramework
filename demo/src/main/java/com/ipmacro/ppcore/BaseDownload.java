package com.ipmacro.ppcore;

import java.util.ArrayList;
import java.util.List;

public class BaseDownload {
    // FIXME: 2017/4/26 p2p fileId使用旧版，怀疑未释放downloader，因此每次start加进来，每次换台，切源所有downloader release
    public static boolean isCanLog = false;
    public static final String TAG = "BaseDownload";
    public static List<BaseDownload> mBaseDownloadList = new ArrayList<>();
    public static int BLOCK_SIZE = 10000;
    public String mSrcUrl, mLowUrl, mHeader;

    public BaseDownload() {
        addDownloader(this);
    }

    public static void addDownloader(BaseDownload downloader) {
        if (mBaseDownloadList == null) {
            mBaseDownloadList = new ArrayList<>();
        }
        i("addDownloader size 前: " + mBaseDownloadList.size());
        mBaseDownloadList.add(downloader);
        i("addDownloader size 后: " + mBaseDownloadList.size());
    }

    public static void releaseOtherDownloader(BaseDownload baseDownload) {
        if (mBaseDownloadList == null || mBaseDownloadList.isEmpty()) {
            return;
        }
        i("释放前大小: " + mBaseDownloadList.size());
        for (int i = 0; i < mBaseDownloadList.size(); i++) {
            BaseDownload download = mBaseDownloadList.get(i);
            if (download != null) {
                if (download == baseDownload) {
                    i("播放源跳过: " + download.getSrcUrl());
                    continue;
                }

                i("释放url: " + download.getSrcUrl());
                download.release();
                mBaseDownloadList.remove(i);
                i--;
            }
        }
        //清除
        i("释放后大小: " + mBaseDownloadList.size());
    }

    public static void releaseAllDownloader() {
        if (mBaseDownloadList == null || mBaseDownloadList.isEmpty()) {
            return;
        }
        i("释放前大小: " + mBaseDownloadList.size());
        for (BaseDownload download : mBaseDownloadList) {
            if (download != null) {
                i("释放url: " + download.getSrcUrl());
                download.release();
            }
        }
        //清除
        mBaseDownloadList.clear();
        i("释放后大小: " + mBaseDownloadList.size());
    }

    public static void i(String msg){
        if(isCanLog){
//            Log.i(TAG, msg);
        }
    }

    public void setHeader(String Header) {
        mHeader = Header;
    }

    public boolean prepare() {// Check Media Ready 1.���������,2.��תm3u8
        return true;
    }

    public boolean prepare2() {// Check Media Ready 1.���������,2.��תm3u8
        if (!prepare()) {
            return false;
        }
        setParam(0, BLOCK_SIZE);
        setParam(1, 6000);
        setParam(2, -1);
        setParam(3, 1024 * 1024 * 10);
        return true;
    }

    public int getProgress() {// Media Progress, Data Transform ��תm3u8�����,
        return 100;
    }

    public int getProgress2() {// Download Progress, Data Not Transform δתm3u8
        return 0;
    }

    public void start(String playUrl) {
    }

    public int getRate() {// Media Speed �������ת����m3u8
        return 0;
    }

    public int getRate2() {// Download Speed ��������
        return getRate();
    }

    public int getDuration() {// Current Play Time ��תm3u8��������Ƕ���
        return 5000;
    }

    public int getDuration2() {// Total Play Time, For Live 0
        // ��ֱ��,����vod�й�ʱ��,��ʾ��Ƶ�ĳ���,��λms
        return 0;
    }

    public boolean isEof() {// No More Data Input �������,ת��m3u8���
        return false;
    }

    public boolean isFinish() {// No More Data Output //������ݶ�������
        return false;
    }

    public void seek(int Time) { // vod

    }

    public void cleanUp() {// Clean Up Old Buffer After Seek

    }

    public void stop() {// Stop Input, Keep Output ǿ��ֹͣ����,�����ܻ��л���

    }

    public void release() {

    }

    public void setSrcUrl(String Url) {
        mSrcUrl = Url;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }

    public void setLowUrl(String Url) {
        mLowUrl = Url;
    }

    public String getLowUrl() {
        return mLowUrl;
    }

    public String getPlayUrl() {
        return mSrcUrl;
    }

    public String getPlayUrl(String Ext) {
        return mSrcUrl;
    }

    public void finalize() {
        release();
    }

    public final static int PARAM_BLOCK = 0;
    public final static int PARAM_REQUIRE = 1;
    public final static int PARAM_FULL = 2;
    public final static int PARAM_LIMIT = 3;

    public void setParam(int Type, int Value) {

    }
}
