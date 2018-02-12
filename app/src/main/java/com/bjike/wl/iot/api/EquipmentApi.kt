package com.bjike.issp.api

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.bean.SoftwareBean
import retrofit2.http.*
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface EquipmentApi {
    /**
     * 加入ws
     */
    @FormUrlEncoded
    @POST("/redis/workerman/bind")
    fun bindPost(@FieldMap  fieldMap:Map<String,String>): Observable<JsonResult<String>>

    /**
     * 获取传感器列表
     */
    @GET("/redis/platform/singleSensor")
    fun getEquipmentData(): Observable<JsonResult<MutableList<EquipmentBean>>>

    /**
     * 获取软件技术列表
     */
    @GET("/redis/platform/application")
    fun getSoftwareData(): Observable<JsonResult<MutableList<SoftwareBean>>>

    /**
     * 获取我的传感器列表
     */
    @GET("/redis/platform/singleSensor")
    fun getEquipmentManagementData(): Observable<JsonResult<MutableList<EquipmentBean>>>

    /**
     * 获取我的传感器设置列表
     */
    @GET("/redis/platform/singleSensor")
    fun getEquipmentControlData(): Observable<JsonResult<MutableList<EquipmentBean>>>


    /**
     * 组合传感器
     */
    @FormUrlEncoded
    @POST("/redis/workerman/bind")
    fun postSoftwareCombination(@FieldMap  fieldMap:MutableMap<String,String>): Observable<JsonResult<String>>

}