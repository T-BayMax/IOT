package com.bjike.issp.mvp.model

import android.content.Context
import com.bjike.issp.api.EquipmentApi
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.contract.EquipmentContract
import com.bjike.issp.network.RetrofitClient
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.network.NetworkUrl
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentModel
     constructor( val context: Context) : EquipmentContract.Model {

        override fun getEquipmentData(  fieldMap:Map<String,String>): Observable<JsonResult<MutableList<EquipmentBean>>>? {
            val retrofitClient = RetrofitClient.getInstance(context, NetworkUrl.BASE_URL)
            val apiService = retrofitClient.create(EquipmentApi::class.java)
           return apiService?.getEquipmentData()

        }
    }