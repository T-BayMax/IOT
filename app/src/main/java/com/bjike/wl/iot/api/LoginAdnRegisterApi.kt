package com.bjike.wl.iot.api

import com.bjike.issp.bean.JsonResult
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by T-BayMax on 2017/12/1.
 */
interface LoginAdnRegisterApi {
    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("/redis/app/code")
    fun getCode(@FieldMap fieldMap:Map<String,String>): Observable<JsonResult<String>>

    /***
     * 注册
     */
    @FormUrlEncoded
    @POST("/redis/app/register_user")
    fun postRegister(@FieldMap fieldMap:Map<String,String>): Observable<JsonResult<String>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/redis/app/login")
    fun postLogin(@FieldMap fieldMap:Map<String,String>): Observable<JsonResult<String>>

}