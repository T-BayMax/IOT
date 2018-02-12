package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface WorkermanContract {
    interface View {
        fun bind(results: String)
        fun showError(errorString: String)

    }

    interface Model {

        fun bind(fieldMap: Map<String, String>): Observable<JsonResult<String>>?
    }

    interface Presenter {

        fun bind(fieldMap: Map<String, String>)
    }
}