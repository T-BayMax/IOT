package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/12/13 14:25
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
interface PortfolioContract {
    interface View {
        fun  postSoftwareCombination(results: String)
        fun showError(errorString: String)

    }

    interface Model {

        fun postSoftwareCombination(  fieldMap:MutableMap<String,String>): Observable<JsonResult<String>>?
    }

    interface Presenter {

        fun postSoftwareCombination(  fieldMap:MutableMap<String,String>)
    }
}