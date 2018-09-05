package com.cyou.xiyou.cyou.common.util;

import android.content.Context;
import android.content.CursorLoader;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.cyou.xiyou.cyou.common.R;
import com.cyou.xiyou.cyou.common.album.AlbumItem;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 003 on 2017-05-22.
 */
public class ResourceUtil
{
    public static String getString(Context context, int id)
    {
        return context.getResources().getString(id);
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int id)
    {
        Drawable drawable = null;

        if(id > 0)
        {
            try
            {
                drawable = context.getResources().getDrawable(id);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }

        return drawable;
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int id)
    {
        return context.getResources().getColor(id);
    }

    public static float getDimension(Context context, int id)
    {
        return context.getResources().getDimension(id);
    }

    public static String getMetaValue(Context context, String metaKey)
    {
        String metaValue = null;

        if(context != null && metaKey != null)
        {
            try
            {
                Bundle metaData = null;
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

                if(info != null)
                {
                    metaData = info.metaData;
                }

                Object value = metaData == null? null: metaData.get(metaKey);

                if(value != null)
                {
                    if(value instanceof String)
                    {
                        metaValue = (String)value;
                    }
                    else
                    {
                        metaValue = value.toString();
                    }
                }
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }

        return metaValue;
    }

    public static Uri getAssetURI(String assetPath)
    {
        return Uri.parse("asset:///" + assetPath);
    }

    public static Uri getDrawableURI(Context context, int drawableId)
    {
        String url = "res://" + context.getPackageName() + "/" + drawableId;
        return Uri.parse(url);
    }

    public static Uri getFileURI(String path)
    {
        return Uri.parse("file://" + path);
    }

    public static int getGradientColor(float fraction, int startARGB, int endARGB)
    {
        int startA = (startARGB >> 24) & 0xFF;
        int startR = (startARGB >> 16) & 0xFF;
        int startG = (startARGB >> 8) & 0xFF;
        int startB = startARGB & 0xFF;
        int endA = (endARGB >> 24) & 0xFF;
        int endR = (endARGB >> 16) & 0xFF;
        int endG = (endARGB >> 8) & 0xFF;
        int endB = endARGB & 0xFF;
        int a = (startA + (int)(fraction * (endA - startA))) << 24;
        int r = (startR + (int)(fraction * (endR - startR))) << 16;
        int g = (startG + (int)(fraction * (endG - startG))) << 8;
        int b = startB + (int)(fraction * (endB - startB));
        return a | r | g | b;
    }

    public static List<AlbumItem> getAlbumPictures(Context context)
    {
        List<AlbumItem> itemList = new ArrayList<>();

        try
        {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " not like ?";
            String[] selectionArgs = {"%drawable%"};
            String order = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " asc, " + MediaStore.Images.Media._ID + " desc";
            CursorLoader loader = new CursorLoader(context, uri, projection, selection, selectionArgs, order);
            Cursor cursor = loader.loadInBackground();

            if(cursor.getCount() > 0)
            {
                AlbumItem item;
                int dataColumnIndex;
                String path, bucketDisplayName;
                File file;

                while(cursor.moveToNext())
                {
                    dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    path = cursor.getString(dataColumnIndex);
                    file = new File(path);

                    if(!file.exists() || !file.isFile() || file.length() == 0)
                    {
                        continue;
                    }

                    dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    bucketDisplayName = cursor.getString(dataColumnIndex);
                    item = new AlbumItem();
                    item.setPath(path);
                    item.setBucketDisplayName(bucketDisplayName);
                    itemList.add(item);
                }
            }

            cursor.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }

        return itemList;
    }

    public static void showGif(SimpleDraweeView imageView, Uri uri)
    {
        Object tag = imageView.getTag(R.id.gif_uri);
        String uriStr = String.valueOf(uri);

        if(tag == null || !tag.equals(uriStr))
        {
            imageView.setTag(R.id.gif_uri, uriStr);
            DraweeController draweeController = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
            imageView.setController(draweeController);
        }
    }

    public static Bitmap createScreenCapture(View view)
    {
        return createScreenCapture(view, 0, false);
    }

    public static Bitmap createScreenCapture(View view, int bgColor, boolean transparent)
    {
        Bitmap bitmap = null;

        try
        {
            Config config = transparent? Config.ARGB_4444: Config.RGB_565;
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
            Canvas canvas = new Canvas(bitmap);

            if(bgColor != 0)
            {
                canvas.drawColor(bgColor);
            }

            view.setDrawingCacheEnabled(true);
            view.draw(canvas);
            view.setDrawingCacheEnabled(false);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }

        return bitmap;
    }

    public static String cacheBitmap(Context context, Bitmap bitmap)
    {
        return cacheBitmap(context, bitmap, 90, CompressFormat.JPEG);
    }

    public static String cacheBitmap(Context context, Bitmap bitmap, int quality, CompressFormat format)
    {
        return cacheBitmap(context, bitmap, quality, format, 0, 0);
    }

    public static String cacheBitmap(Context context, Bitmap bitmap, int quality, CompressFormat format, int width, int height)
    {
        String path = null;

        if(bitmap != null && !bitmap.isRecycled())
        {
            try
            {
                if(width > 0 && height > 0)
                {
                    Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    bitmap.recycle();
                    bitmap = newBitmap;
                }

                String fileName = Digest.computeMD5("BitmapCache:" + System.currentTimeMillis());
                File file = new File(StorageUtil.getCacheDir(context), fileName);
                path = file.getPath();
                IOUtil.writeBitmap(bitmap, path, quality, format);
                bitmap.recycle();
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }

        return path;
    }
}