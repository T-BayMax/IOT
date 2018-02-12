package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import com.bjike.wl.iot.bean.ListContentBean
import com.bjike.wl.iot.bean.OriginalityBean
import com.bjike.wl.iot.bean.SoftwareBean
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface OriginalityContract {
    interface View {
        fun  getOriginalityListData(results: MutableList<OriginalityBean>)
        fun showError(errorString: String)

    }

    interface Model {

        fun getOriginalityListData(  fieldMap:Map<String,String>): Observable<JsonResult<ListContentBean<MutableList<OriginalityBean>>>>?
    }

    interface Presenter {

         fun getOriginalityListData(  fieldMap:Map<String,String>)
    }
}