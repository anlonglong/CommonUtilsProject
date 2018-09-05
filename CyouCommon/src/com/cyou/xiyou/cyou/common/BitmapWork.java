package com.cyou.xiyou.cyou.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.LruCache;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;


/**
 * @author anlonglong on 2018/7/27.
 * Email： 940752944@qq.com
 * bitmap进行压缩的类
 */
public class BitmapWork {

    private LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount() / 1024;
        }
    };

    public Bitmap loadBitmap(Context context, int resId, @Nullable BitmapCompressListener listener) {
        Bitmap bitmap = getBitmapFromMemoryCache(String.valueOf(resId));
        if (null == bitmap) {
            AsyncTask<Integer, Void, Bitmap> execute = new BitmapWorkTask(context, this, listener).execute(resId);
            try {
                bitmap = execute.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    public Bitmap getBitmapFromMemoryCache(String key) {
        return mLruCache.get(key);
    }


    public void addBitmapFromMemoryCache(String key, Bitmap bitmap) {
        if (null == mLruCache.get(key)) {
            mLruCache.put(key, bitmap);
        }
    }

    public static class BitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {

        private BitmapCompressListener mListener;
        private BitmapWork mBitmapWork;
        private WeakReference<Context> mWeakReference;

        public BitmapWorkTask(Context context, BitmapWork bitmapWork, BitmapCompressListener listener) {
            if (null == mWeakReference) {
                mWeakReference = new WeakReference<>(context);
            }
            mBitmapWork = bitmapWork;
            mListener = listener;
        }

        @Override
        protected void onPreExecute() {
            if (null != mListener) {
                mListener.onPreCompress();
            }
        }

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            if (this.isCancelled()) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeResource(mWeakReference.get().getResources(), integers[0], options);
            mBitmapWork.addBitmapFromMemoryCache(String.valueOf(integers[0]), bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (null != mListener) {
                mListener.onCompressFinish(bitmap);
            }
        }
    }

    interface BitmapCompressListener {
        /**
         * 执行压缩方法前的回调
         */
        void onPreCompress();

        /**
         * 压缩完成回调
         *
         * @param bitmap 压缩后的bitmap
         */
        void onCompressFinish(Bitmap bitmap);

    }

}
