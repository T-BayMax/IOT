package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.EquipmentBean
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface EquipmentManagementContract {
    interface View {
        /**
         * 获取我的列表
         */
        fun getEquipmentManagementData(results: MutableList<EquipmentBean>)
        fun showError(errorString: String)

    }

    interface Model {

        fun getEquipmentManagementData(fieldMap:Map<String,String>): Observable<JsonResult<MutableList<EquipmentBean>>>?
    }

    interface Presenter {

         fun getEquipmentManagementData(fieldMap:Map<String,String>)
    }
}