package top.zibin.luban;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Luban implements Handler.Callback {
  private static final String TAG = "Luban";
  private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

  private static final int MSG_COMPRESS_SUCCESS = 0;
  private static final int MSG_COMPRESS_START = 1;
  private static final int MSG_COMPRESS_ERROR = 2;

  private String diskCacheDir;
  private File file;
  private OnCompressListener onCompressListener;

  private Handler mHandler;

  private Luban(Builder builder) {
    this.diskCacheDir = builder.diskCacheDir;
    this.file = builder.file;
    this.onCompressListener = builder.onCompressListener;
    mHandler = new Handler(Looper.getMainLooper(), this);
  }

  public static Builder with(Context context) {
    return new Builder(context);
  }

  /**
   * Returns a file with a cache audio name in the private cache directory.
   *
   * @param context
   *     A context.
   */
  private File getImageCacheFile(Context context) {
    if (getImageCacheDir(context) != null) {
      return new File(getImageCacheDir(context) + "/" + System.currentTimeMillis() + (int) (Math.random() * 1000) + ".jpg");
    }
    return null;
  }

  /**
   * Returns a directory with a default name in the private cache directory of the application to
   * use to store retrieved audio.
   *
   * @param context
   *     A context.
   *
   * @see #getImageCacheDir(Context, String)
   */
  @Nullable
  private File getImageCacheDir(Context context) {
    String cacheDir = TextUtils.isEmpty(diskCacheDir)? DEFAULT_DISK_CACHE_DIR: diskCacheDir;
    return getImageCacheDir(context, cacheDir);
  }

  /**
   * Returns a directory with the given name in the private cache directory of the application to
   * use to store retrieved media and thumbnails.
   *
   * @param context
   *     A context.
   * @param cacheName
   *     The name of the subdirectory in which to store the cache.
   *
   * @see #getImageCacheDir(Context)
   */
  @Nullable
  private File getImageCacheDir(Context context, String cacheName) {
    File cacheDir = context.getExternalCacheDir();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // File wasn't able to create a directory, or the result exists but not a directory
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "default disk cache dir is null");
    }
    return null;
  }

  /**
   * start asynchronous compress thread
   */
  @UiThread private void launch(final Context context) {
    if (file == null && onCompressListener != null) {
      onCompressListener.onError(new NullPointerException("image file cannot be null"));
    }

      ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
      ThreadFactory threadFactory = threadFactoryBuilder.setNameFormat("Luban-pool-%d").build();
      final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1024), threadFactory);
      threadPoolExecutor.execute(new Runnable() {
          @Override
          public void run() {
              try {
                  mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_START));
                  File result = new Engine(file, getImageCacheFile(context)).compress();
                  mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, result));
              } catch (IOException e) {
                  mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, e));
              }
              threadPoolExecutor.shutdown();
          }
      });
  }

  /**
   * start compress and return the file
   */
  @WorkerThread private File get(final Context context) throws IOException {
    return new Engine(file, getImageCacheFile(context)).compress();
  }

  @Override public boolean handleMessage(Message msg) {
    if (onCompressListener == null) {
      return false;
    }

    switch (msg.what) {
      case MSG_COMPRESS_START:
        onCompressListener.onStart();
        break;
      case MSG_COMPRESS_SUCCESS:
        onCompressListener.onSuccess((File) msg.obj);
        break;
      case MSG_COMPRESS_ERROR:
        onCompressListener.onError((Throwable) msg.obj);
        break;
        default:
    }
    return false;
  }

  public static class Builder {
    private Context context;
    private String diskCacheDir;
    private File file;
    private OnCompressListener onCompressListener;

    Builder(Context context) {
      this.context = context;
    }

    private Luban build() {
      return new Luban(this);
    }

    public Builder load(File file) {
      this.file = file;
      return this;
    }

    public Builder setDiskCacheDir(String diskCacheDir) {
      this.diskCacheDir = diskCacheDir;
      return this;
    }

    public Builder putGear(int gear) {
      return this;
    }

    public Builder setCompressListener(OnCompressListener listener) {
      this.onCompressListener = listener;
      return this;
    }

    public void launch() {
      build().launch(context);
    }

    public File get() throws IOException {
      return build().get(context);
    }
  }
}