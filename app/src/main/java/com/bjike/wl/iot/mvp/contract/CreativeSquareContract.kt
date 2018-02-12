package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.CreativeSquareBean
import com.bjike.wl.iot.bean.EquipmentBean
import io.reactivex.Observable

/**
 * 创意广场
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface CreativeSquareContract {
    interface View {
        fun  getCreativeData(results: MutableList<CreativeSquareBean>)
        fun showError(errorString: String)

    }

    interface Model {

        fun getCreativeData(  fieldMap:Map<String,String>): Observable<JsonResult<MutableList<CreativeSquareBean>>>?
    }

    interface Presenter {

         fun getCreativeData(  fieldMap:Map<String,String>)
    }
}