package com.cyou.xiyou.cyou.common.asynwork


/**
 * Created by anlonglong on 2018/5/31.
 * Email： 940752944@qq.com
 */


//异步任务的结果处理类，包括监听异步结果，切换结果所在的线程,泛型是返回值的类型
@SuppressWarnings("all")
class Result<R> {
    var mResult: R? = null
    set(value) {
        if (null != value) {
            field = value
        }
    }
     get() {return if (null != field) field else null}
    private var mSuccess: Boolean = false
    var mThreadHandler: ThreadHolder = ThreadHolder.MAIN
    set(value) {
        if (value !=ThreadHolder.MAIN )
            field = value
    }
    private var mFailCount = 0
    private val mListeners:MutableList<AsynResultListener<R>> by lazy { mutableListOf<AsynResultListener<R>>() }
    private lateinit var mException: Exception

    fun addAsynResultListener(listener: AsynResultListener<R>) { mListeners += listener}

    fun removeAsynResultLisenter(listener: AsynResultListener<R>) {mListeners -= listener}

    internal fun setSuccess(result: R) {
        if (mSuccess) return
        mResult = result
        mSuccess = true
        notifyResultListener()
    }

   internal fun setError(exception: Exception) {
        ++mFailCount
        mException = exception
        mSuccess = false
        notifyResultListener()
    }

    private fun notifyResultListener() = mListeners.forEach {
        it.onResult(this)
    }
}