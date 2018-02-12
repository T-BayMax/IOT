package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.mvp.contract.SoftwareContract
import com.bjike.wl.iot.mvp.model.SoftwareModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 * /
 */
class SoftwarePresenter
constructor(private val context: Context, view: SoftwareContract.View)
    : SoftwareContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: SoftwareContract.View? = null
    val mModel: SoftwareModel by lazy {
        SoftwareModel(context)
    }

    init {
        mView = view
        mContext = context
    }

    override fun getSoftwareData(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<MutableList<SoftwareBean>>>? = mContext?.let { mModel.getSoftwareData(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<MutableList<SoftwareBean>> ->
            if (result.code == 200)
                mView?.getSoftwareData(result.data )
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}