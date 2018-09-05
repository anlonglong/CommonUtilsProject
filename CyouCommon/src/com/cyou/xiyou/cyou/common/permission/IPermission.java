package com.cyou.xiyou.cyou.common.permission;

/**
 * @author  anlonglong on 2018/7/23.
 * Email： 940752944@qq.com
 */
@SuppressWarnings("ALL")
public interface IPermission {
    /**
     * 用户授权时候的回调
     * @param requestCode
     * @param permission
     */
    void hasPermission(int requestCode,String... permission);

    /**
     * 用户未授权的时候的回调
     * @param requestCode
     * @param permission
     */
    void permissionDenied(int requestCode,String... permission);

    /**
     * 用户未授权并且点击了不再询问的回调
     * @param requestCode
     * @param permission
     */
    void onNeverAskAgain(int requestCode,String... permission);
}
