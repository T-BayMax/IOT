package com.bjike.wl.iot.api

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.CreativeSquareBean
import io.reactivex.Observable
import retrofit2.http.GET


/**
 * 创意广场
 * author：T-Baymax on 2018/2/1 15:32
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface CreativeSquareApi {
    /**
     * 创意广场列表
     */
    @GET("/redis/platform/creative")
    fun getCreativeData(): Observable<JsonResult<MutableList<CreativeSquareBean>>>

}