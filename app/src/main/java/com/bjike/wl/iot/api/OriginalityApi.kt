package com.bjike.wl.iot.api

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.ListContentBean
import com.bjike.wl.iot.bean.OriginalityBean
import retrofit2.http.*
import io.reactivex.Observable

/**
 * author：T-Baymax on 2018/1/25 17:02
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface OriginalityApi {
    /**
     * 获取传感器列表
     */
    @GET("/redis/platform/originalityList")
    fun getOriginalityList(): Observable<JsonResult<ListContentBean<MutableList<OriginalityBean>>>>

}