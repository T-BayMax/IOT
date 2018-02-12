package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.mvp.contract.LoginContract
import com.bjike.wl.iot.mvp.model.LoginModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class LoginPresenter
constructor(private val context: Context, view: LoginContract.View)
    : LoginContract.Presenter, BasePresenter() {
    var mContext: Context? = null
    var mView: LoginContract.View? = null
    val mModel: LoginModel by lazy {
        LoginModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun postLogin(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<String>>? = mContext?.let { mModel.postLogin(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<String> ->
            if (result.code == 200)
                mView?.postLogin(result.data)
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }


}