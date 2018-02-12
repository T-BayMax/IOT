package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import io.reactivex.Observable

/**
 * Created by T-BayMax on 2017/12/2.
 */
interface LoginContract {
    interface View {
        fun postLogin(results:String)
        fun showError(errorString: String)

    }

    interface Model {

        fun postLogin(fieldMap: Map<String, String>): Observable<JsonResult<String>>?
    }

    interface Presenter {

        fun postLogin(fieldMap: Map<String, String>)
    }
}