package com.bjike.issp.mvp.contract

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.EquipmentBean
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface EquipmentContract {
    interface View {
        fun  getEquipmentData(results: MutableList<EquipmentBean>)
        fun showError(errorString: String)

    }

    interface Model {

        fun getEquipmentData(  fieldMap:Map<String,String>): Observable<JsonResult<MutableList<EquipmentBean>>>?
    }

    interface Presenter {

         fun getEquipmentData(  fieldMap:Map<String,String>)
    }
}