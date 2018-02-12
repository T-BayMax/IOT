package com.bjike.wl.iot.mvp.model

import android.content.Context
import com.bjike.issp.api.EquipmentApi
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.network.RetrofitClient
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.mvp.contract.SoftwareContract
import com.bjike.wl.iot.network.NetworkUrl
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class SoftwareModel
     constructor( val context: Context) : SoftwareContract.Model {

        override fun getSoftwareData(  fieldMap:Map<String,String>): Observable<JsonResult<MutableList<SoftwareBean>>>? {
            val retrofitClient = RetrofitClient.getInstance(context, NetworkUrl.BASE_URL)
            val apiService = retrofitClient.create(EquipmentApi::class.java)
           return apiService?.getSoftwareData()

        }
    }