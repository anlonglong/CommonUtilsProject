package com.cyou.xiyou.cyou.common.album;

import android.net.Uri;

import com.cyou.xiyou.cyou.common.util.ResourceUtil;

import java.io.Serializable;

/**
 * Created by 003 on 2017-05-31.
 */
public class AlbumItem implements Serializable
{
    private static final long serialVersionUID = 3158917834910031684L;

    private String path;

    private String bucketDisplayName;

    public String getPath()
    {
        return this.path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getBucketDisplayName()
    {
        return this.bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName)
    {
        this.bucketDisplayName = bucketDisplayName;
    }

    public Uri getUri()
    {
        return ResourceUtil.getFileURI(getPath());
    }
}