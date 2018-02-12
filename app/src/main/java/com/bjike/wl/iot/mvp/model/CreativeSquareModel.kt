package com.bjike.wl.iot.mvp.model

import android.content.Context
import com.bjike.issp.api.EquipmentApi
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.network.RetrofitClient
import com.bjike.wl.iot.api.CreativeSquareApi
import com.bjike.wl.iot.bean.CreativeSquareBean
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.CreativeSquareContract
import com.bjike.wl.iot.mvp.contract.EquipmentControlContract
import com.bjike.wl.iot.network.NetworkUrl
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CreativeSquareModel
     constructor( val context: Context) : CreativeSquareContract.Model {

        override fun getCreativeData(fieldMap:Map<String,String>): Observable<JsonResult<MutableList<CreativeSquareBean>>>? {
            val retrofitClient = RetrofitClient.getInstance(context, NetworkUrl.BASE_URL)
            val apiService = retrofitClient.create(CreativeSquareApi::class.java)
           return apiService?.getCreativeData()

        }
    }