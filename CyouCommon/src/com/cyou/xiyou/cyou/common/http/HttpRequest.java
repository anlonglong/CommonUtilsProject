package com.cyou.xiyou.cyou.common.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.cyou.xiyou.cyou.common.R;
import com.cyou.xiyou.cyou.common.util.LogUtil;
import com.cyou.xiyou.cyou.common.util.ResourceUtil;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpClientException;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.exception.HttpNetException;
import com.litesuits.http.exception.HttpServerException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.HttpBody;
import com.litesuits.http.request.content.JsonBody;
import com.litesuits.http.request.content.UrlEncodedFormBody;
import com.litesuits.http.response.Response;

import java.util.Map;

/**
 * Created by 003 on 2017-04-29.
 */
public class HttpRequest
{
    private static final String TAG = HttpRequest.class.getSimpleName();

    public static final int ERROR_PRECONDITION_REFUSED = -30030;

    public static final int ERROR_CANCELLED = -30031;

    public static final int ERROR_PARSE_DATA_FAIL = -30032;

    public static final int ERROR_OTHER = -30099;

    private static HttpRequest instance;

    private static ResultInterceptor resultInterceptor;

    private Context context;

    private LiteHttp liteHttp;

    public synchronized static HttpRequest getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new HttpRequest();
        }

        instance.context = context.getApplicationContext();
        return instance;
    }

    private HttpRequest()
    {}

    public LiteHttp getLiteHttp()
    {
        if(liteHttp == null)
        {
            liteHttp = LiteHttp.build(context).setConnectTimeout(5000).setSocketTimeout(5000)
                    .setDefaultMaxRedirectTimes(3).setDefaultMaxRetryTimes(3).create();
        }

        return liteHttp;
    }

    public <T extends HttpOutput> AbstractRequest<?> executeUrlEncodedForm(@NonNull HttpInput httpInput, @NonNull Class<T> clazz, HttpRequestListener<T> listener)
    {
        return execute(httpInput, new UrlEncodedFormBody(httpInput.getParams()), clazz, listener);
    }

    public <T extends HttpOutput> AbstractRequest<?> executeJSON(@NonNull HttpInput httpInput, @NonNull Class<T> clazz, HttpRequestListener<T> listener)
    {
        return execute(httpInput, new JsonBody(httpInput.getJSONString()), clazz, listener);
    }

    public <T extends HttpOutput> AbstractRequest<?> execute(@NonNull final HttpInput httpInput, HttpBody httpBody, @NonNull final Class<T> clazz, final HttpRequestListener<T> listener)
    {
        final String url = httpInput.getURL();
        StringRequest request = new StringRequest(url);
        request.setHttpBody(httpBody);
        request.setMethod(httpInput.getHttpMethod());

        Map<String, String> headers = httpInput.getHeaders();

        if(headers != null && !headers.isEmpty())
        {
            request.addHeader(headers);
        }

        request.setHttpListener(new HttpListener<String>()
        {
            @Override
            public void onSuccess(String result, Response<String> response)
            {
                T data = parseJSON(result, clazz);

                if(data != null)
                {
                    data.setOriginalData(result);
                    onEnd(data.isSuccess(), data, response);
                }
                else
                {
                    String errorInfo = ResourceUtil.getString(context, R.string.parse_data_fail);
                    onEnd(false, new ErrorHttpOutput(ErrorType.ParseDataFail, errorInfo, result), response);
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response)
            {
                String errorInfo;

                if(e instanceof HttpServerException)
                {
                    errorInfo = ((HttpServerException)e).getExceptionType().chiReason;
                }
                else if(e instanceof HttpClientException)
                {
                    errorInfo = ((HttpClientException)e).getExceptionType().chiReason;
                }
                else if(e instanceof HttpNetException)
                {
                    errorInfo = ((HttpNetException)e).getExceptionType().chiReason;
                }
                else
                {
                    errorInfo = e.getMessage();
                    errorInfo = errorInfo == null? response.getResult(): errorInfo;
                }

                onEnd(false, new ErrorHttpOutput(ErrorType.Other, errorInfo, null), response);
            }

            @Override
            public void onCancel(String str, Response<String> response)
            {
                String errorInfo = ResourceUtil.getString(context, R.string.request_cancelled);
                onEnd(false, new ErrorHttpOutput(ErrorType.Cancelled, errorInfo, null), response);
            }

            @SuppressWarnings("unchecked")
            private void onEnd(boolean success, HttpOutput data, Response<String> response)
            {
                String originalData = data.getOriginalData();

                if(success)
                {
                    LogUtil.i(TAG, "URL=" + url + "\nInput=" + httpInput.getInputData() + "\nOutput=" + originalData);
                }
                else
                {
                    LogUtil.e(TAG, "Success=false, ErrorInfo=" + data.getResultInfo() + ", ErrorCode=" + data.getResultCode());
                    LogUtil.e(TAG, "URL=" + url + "\nInput=" + httpInput.getInputData() + "\nOutput=" + originalData);
                }

                if(resultInterceptor != null)
                {
                    resultInterceptor.onResult(data);
                }

                if(listener != null)
                {
                    if(success)
                    {
                        listener.onSuccess((T)data, response);
                    }
                    else
                    {
                        listener.onFailure(data, response);
                    }
                }
            }
        });
        getLiteHttp().executeAsync(request);
        return request;
    }

    private <T extends HttpOutput> T parseJSON(String data, Class<T> clazz)
    {
        T result = null;

        try
        {
            result = JSONObject.parseObject(data, clazz);
        }
        catch(Throwable t)
        {
            LogUtil.e(TAG, t.getMessage(), t);
        }

        return result;
    }

    public static void setResultInterceptor(ResultInterceptor resultInterceptor)
    {
        LogUtil.d(TAG, "setResultInterceptor, resultInterceptor=" + resultInterceptor);
        HttpRequest.resultInterceptor = resultInterceptor;
    }

    public static class ErrorHttpOutput implements HttpOutput
    {
        private static final long serialVersionUID = -7425850597102836932L;

        private final int resultCode;

        private final String resultInfo;

        private final String originalData;

        private final boolean success;

        private final boolean cancelled;

        private ErrorHttpOutput(ErrorType errorType, String resultInfo, String originalData)
        {
            this.resultCode = errorType.value;
            this.resultInfo = resultInfo;
            this.originalData = originalData;
            this.success = false;
            this.cancelled = errorType == ErrorType.Cancelled;
        }

        @Override
        public int getResultCode()
        {
            return this.resultCode;
        }

        @Override
        public String getResultInfo()
        {
            return this.resultInfo;
        }

        @Override
        public String getOriginalData()
        {
            return this.originalData;
        }

        @Override
        public void setOriginalData(String originalData)
        {}

        @Override
        public boolean isSuccess()
        {
            return this.success;
        }

        @Override
        public boolean isCancelled()
        {
            return this.cancelled;
        }
    }

    private enum ErrorType
    {
        //取消请求
        Cancelled(ERROR_CANCELLED),
        //数据解析失败
        ParseDataFail(ERROR_PARSE_DATA_FAIL),
        //其他错误
        Other(ERROR_OTHER);

        private int value;

        ErrorType(int value)
        {
            this.value = value;
        }
    }

    public interface HttpRequestListener<T extends HttpOutput>
    {
        void onSuccess(T data, Response<String> response);

        void onFailure(HttpOutput data, Response<String> response);
    }

    public interface ResultInterceptor
    {
        void onResult(HttpOutput data);
    }
}