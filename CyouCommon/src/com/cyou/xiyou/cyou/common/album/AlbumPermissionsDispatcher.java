package com.cyou.xiyou.cyou.common.album;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import com.litesuits.orm.db.annotation.Default;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

/**
 * Created by 003 on 2017-06-01.
 */
public class AlbumPermissionsDispatcher
{
    public static final int REQUEST_CODE = 3;

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    static void loadWithCheck(AlbumFragment target)
    {
        Context context = target.getContext();

        if(PermissionUtils.hasSelfPermissions(context, PERMISSIONS))
        {
            target.load();
        }
        else
        {
            if(PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSIONS))
            {
                target.showRationaleForAlbum(new AlbumPermissionRequest(target));
            }
            else
            {
                Activity activity = target.getActivity();

                if(activity != null)
                {
                    ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
                }
            }
        }
    }

    static void onRequestPermissionsResult(AlbumFragment target, int requestCode, int[] grantResults)
    {
        switch(requestCode)
        {
            case REQUEST_CODE:
            {
                if(PermissionUtils.verifyPermissions(grantResults))
                {
                    target.load();
                }
                else
                {
                    if(!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSIONS))
                    {
                        target.onAlbumNeverAskAgain();
                    }
                    else
                    {
                        target.onAlbumDenied();
                    }
                }
                break;
            }

            default:
        }
    }

    private static final class AlbumPermissionRequest implements PermissionRequest
    {
        private final AlbumFragment target;

        private AlbumPermissionRequest(AlbumFragment target)
        {
            this.target = target;
        }

        @Override
        public void proceed()
        {
            Activity activity = target == null? null: target.getActivity();

            if(activity != null)
            {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
            }
        }

        @Override
        public void cancel()
        {
            if(target != null)
            {
                target.onAlbumDenied();
            }
        }
    }
}