package com.cyou.xiyou.cyou.common.http;

import com.litesuits.http.request.param.HttpMethods;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 003 on 2017-05-22.
 */
public interface HttpInput extends Serializable
{
    String getURL();

    HttpMethods getHttpMethod();

    Map<String, String> getHeaders();

    String getJSONString();

    Map<String, String> getParams();

    String getInputData();
}