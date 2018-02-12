package com.bjike.wl.iot.mvp.model

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.network.RetrofitClient
import com.bjike.wl.iot.api.LoginAdnRegisterApi
import com.bjike.wl.iot.mvp.contract.LoginContract
import com.bjike.wl.iot.network.NetworkUrl
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class LoginModel
constructor(val context: Context) : LoginContract.Model {

    override fun postLogin(fieldMap: Map<String, String>): Observable<JsonResult<String>> {
        val retrofitClient = RetrofitClient.getInstance(context, NetworkUrl.BASE_URL)
        val apiService = retrofitClient.create(LoginAdnRegisterApi::class.java)
        return apiService?.postLogin(fieldMap)!!

    }


}