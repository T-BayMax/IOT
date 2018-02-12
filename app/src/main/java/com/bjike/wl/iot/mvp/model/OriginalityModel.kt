package com.bjike.wl.iot.mvp.model

import android.content.Context
import com.bjike.issp.api.EquipmentApi
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.network.RetrofitClient
import com.bjike.wl.iot.api.OriginalityApi
import com.bjike.wl.iot.bean.ListContentBean
import com.bjike.wl.iot.bean.OriginalityBean
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.mvp.contract.OriginalityContract
import com.bjike.wl.iot.mvp.contract.SoftwareContract
import com.bjike.wl.iot.network.NetworkUrl
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class OriginalityModel
     constructor( val context: Context) : OriginalityContract.Model {

        override fun getOriginalityListData(  fieldMap:Map<String,String>): Observable<JsonResult<ListContentBean<MutableList<OriginalityBean>>>>? {
            val retrofitClient = RetrofitClient.getInstance(context, NetworkUrl.BASE_URL)
            val apiService = retrofitClient.create(OriginalityApi::class.java)
           return apiService?.getOriginalityList()

        }
    }