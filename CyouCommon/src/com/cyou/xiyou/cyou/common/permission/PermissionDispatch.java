package com.cyou.xiyou.cyou.common.permission;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.cyou.xiyou.cyou.common.util.ResourceUtil;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author anlonglong on 2018/7/23.
 * Email： 940752944@qq.com
 */
public class PermissionDispatch implements EasyPermissions.PermissionCallbacks {

    private Activity mActivity;
    private Fragment mFragment;
    private String[] mCameraPerm = {Manifest.permission.CAMERA};

    /**
     * 小米手机调用相机并且获取拍照路径的时候，需要两个权限 1.相机权限 2.SD卡权限
     */
    private String[] mXiaoMiCameraPerm = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String PRODUCT_BRAND = "Xiaomi";
    private IPermission mIPermission;
    private String[] mAskPermission = new String[]{};

    public PermissionDispatch(Activity activity) {
        mActivity = activity;
    }

    public PermissionDispatch(Fragment fragment) {
        mFragment = fragment;
    }

    public void checkPermissionForActivity(int requestCode, IPermission permission, String noPermissionDesc, String... perm) {
        this.mIPermission = permission;
        checkPermissionForActivity(mActivity, requestCode, noPermissionDesc, -1, perm);
    }

    public void checkPermissionForActivity(int requestCode, IPermission permission, int resIdNoPermissionDesc, String... perm) {
        this.mIPermission = permission;
        checkPermissionForActivity(mActivity, requestCode, null, resIdNoPermissionDesc, perm);
    }

    public void checkPermissionFragment(int requestCode, IPermission permission, String noPermissionDesc, String... perm) {
        this.mIPermission = permission;
        checkPermissionForFragment(mFragment, requestCode, noPermissionDesc, -1, perm);
    }


    public void checkPermissionFragment(int requestCode, IPermission permission, int resIdNoPermissionDesc, String... perm) {
        this.mIPermission = permission;
        checkPermissionForFragment(mFragment, requestCode, null, resIdNoPermissionDesc, perm);
    }

    private <T extends Activity> void checkPermissionForActivity(T t, int requestCode, String noPermissionDesc, int resIdNoPermissionDesc, String... perm) {
        this.mAskPermission = perm;
        if (null != t) {
            if (EasyPermissions.hasPermissions(t, perm)) {
                if (null != mIPermission) {
                    mIPermission.hasPermission(requestCode, perm);
                }
            } else {
                EasyPermissions.requestPermissions(t, null != noPermissionDesc ? noPermissionDesc : ResourceUtil.getString(mActivity, resIdNoPermissionDesc), requestCode, perm);
            }
        }
    }


    private <T extends Fragment> void checkPermissionForFragment(T t, int requestCode, String noPermissionDesc, int resIdNoPermissionDesc, String... perm) {
        this.mAskPermission = perm;
        if (null != t) {
            if (EasyPermissions.hasPermissions(t.getActivity(), perm)) {
                if (null != mIPermission) {
                    mIPermission.hasPermission(requestCode, perm);
                }
            } else {
                EasyPermissions.requestPermissions(t, null != noPermissionDesc ? noPermissionDesc : ResourceUtil.getString(t.getActivity(), resIdNoPermissionDesc), requestCode, perm);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if (null != mIPermission) {
            mIPermission.hasPermission(requestCode, perms.toArray(new String[perms.size()]));
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //mActivity中的回调
        if (null != mActivity) {
            if (null != mIPermission) {
                if (EasyPermissions.somePermissionDenied(mActivity, perms.toArray(new String[perms.size()]))) {
                    mIPermission.permissionDenied(requestCode, perms.toArray(new String[perms.size()]));
                } else {
                    mIPermission.onNeverAskAgain(requestCode, perms.toArray(new String[perms.size()]));
                }
            }
        }

        //mFragment中的回调
        if (null != mFragment) {
            if (null != mIPermission) {
                if (EasyPermissions.somePermissionDenied(mFragment, perms.toArray(new String[perms.size()]))) {
                    mIPermission.permissionDenied(requestCode, perms.toArray(new String[perms.size()]));
                } else {
                    mIPermission.onNeverAskAgain(requestCode, perms.toArray(new String[perms.size()]));
                }
            }
        }
    }

}
