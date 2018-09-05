package com.cyou.xiyou.cyou.common.album;

import android.net.Uri;

import com.cyou.xiyou.cyou.common.util.ResourceUtil;

import java.io.Serializable;

/**
 * Created by 003 on 2017-05-31.
 */
public class AlbumDir implements Serializable
{
    private static final long serialVersionUID = 6619774873121348918L;

    private String name;

    private int photoCount;

    private String firstPath;

    private boolean all;

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPhotoCount()
    {
        return this.photoCount;
    }

    public void setPhotoCount(int photoCount)
    {
        this.photoCount = photoCount;
    }

    public String getFirstPath()
    {
        return this.firstPath;
    }

    public void setFirstPath(String firstPath)
    {
        this.firstPath = firstPath;
    }

    public boolean isAll()
    {
        return this.all;
    }

    public void setAll(boolean all)
    {
        this.all = all;
    }

    public Uri getFirstUri()
    {
        return ResourceUtil.getFileURI(getFirstPath());
    }
}