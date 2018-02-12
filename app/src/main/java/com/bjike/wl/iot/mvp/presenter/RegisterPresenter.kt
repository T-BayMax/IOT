package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.mvp.contract.RegisterContract.Presenter
import com.bjike.wl.iot.mvp.contract.RegisterContract.View
import com.bjike.wl.iot.mvp.model.RegisterModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class RegisterPresenter
constructor(private val context: Context, view: View)
    : Presenter, BasePresenter() {
    var mContext: Context? = null
    var mView: View? = null
    val mModel: RegisterModel by lazy {
        RegisterModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun getCode(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<String>>? = mContext?.let { mModel.getCode(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<String> ->
            if (result.code == 200)
                mView?.getCode(result.msg)
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

    override fun postRegister(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<String>>? = mContext?.let { mModel.postRegister(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<String> ->
            if (result.code == 200)
                mView?.postRegister(result.msg)
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }


}