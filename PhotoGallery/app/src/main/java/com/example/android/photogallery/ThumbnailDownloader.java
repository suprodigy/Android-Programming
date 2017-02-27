package com.example.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jr on 2017-02-27.
 */

public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private LruCache<String, Bitmap> mImageCache;

    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler;
    private ThumbnailDownloadedListener<T> mThumbnailDownloadedListener;

    public interface ThumbnailDownloadedListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadedListener(ThumbnailDownloadedListener<T> listener) {
        mThumbnailDownloadedListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
        mImageCache = new LruCache<>(40);
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);

        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);

            mRequestHandler
                    .obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final T target) {
        try {
            final String url = mRequestMap.get(target);
            if (url == null) {
                return;
            }

            Bitmap bitmap = mImageCache.get(url);

            if (bitmap == null) {
                byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                mImageCache.put(url, bitmap);
                Log.i(TAG, "Bitmap created");
            }

            final Bitmap realBitmap = bitmap;

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRequestMap.get(target) == null) {
                        return;
                    }
                    mRequestMap.remove(target);
                    mThumbnailDownloadedListener.onThumbnailDownloaded(target, realBitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

}
