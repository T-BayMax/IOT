package com.bjike.wl.iot.mvp.contract

import com.bjike.issp.bean.JsonResult
import io.reactivex.Observable

/**
 * Created by T-BayMax on 2017/12/1.
 */
interface RegisterContract {
    interface View {
        /***
         * 获取验证码
         */
        fun getCode(results: String)

        /**
         * 注册
         */
        fun postRegister(results: String)

        fun showError(errorString: String)

    }

    interface Model {

        fun getCode(fieldMap: Map<String, String>): Observable<JsonResult<String>>
        fun postRegister(fieldMap: Map<String, String>): Observable<JsonResult<String>>
    }

    interface Presenter {

        fun getCode(fieldMap: Map<String, String>)
        fun postRegister(fieldMap: Map<String, String>)
    }
}