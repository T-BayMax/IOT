package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.SoftwareBean
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface SoftwareContract {
    interface View {
        fun  getSoftwareData(results: MutableList<SoftwareBean>)
        fun showError(errorString: String)

    }

    interface Model {

        fun getSoftwareData(  fieldMap:Map<String,String>): Observable<JsonResult<MutableList<SoftwareBean>>>?
    }

    interface Presenter {

         fun getSoftwareData(  fieldMap:Map<String,String>)
    }
}