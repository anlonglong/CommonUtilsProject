package com.cyou.xiyou.cyou.common.http;

import java.io.Serializable;

/**
 * Created by 003 on 2017-05-22.
 */
public interface HttpOutput extends Serializable
{
    int getResultCode();

    String getResultInfo();

    String getOriginalData();

    void setOriginalData(String originalData);

    boolean isSuccess();

    boolean isCancelled();
}