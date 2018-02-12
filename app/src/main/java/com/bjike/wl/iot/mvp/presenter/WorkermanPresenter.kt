package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.mvp.contract.WorkermanContract
import com.bjike.wl.iot.mvp.model.WorkermanModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class WorkermanPresenter
constructor(private val context: Context, view: WorkermanContract.View)
    : WorkermanContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: WorkermanContract.View? = null
    val mModel: WorkermanModel by lazy {
        WorkermanModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun bind(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<String>>? = mContext?.let { mModel.bind(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<String> ->
            if (result.code == 200)
                mView?.bind(result.msg)
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}