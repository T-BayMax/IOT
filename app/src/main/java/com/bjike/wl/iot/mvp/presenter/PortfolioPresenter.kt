package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.mvp.contract.PortfolioContract
import com.bjike.wl.iot.mvp.model.PortfolioModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class PortfolioPresenter
constructor(private val context: Context, view: PortfolioContract.View)
    : PortfolioContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: PortfolioContract.View? = null
    val mModel: PortfolioModel by lazy {
        PortfolioModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun postSoftwareCombination(fieldMap: MutableMap<String, String>) {
        val observable: Observable<JsonResult<String>>? = mContext?.let { mModel.postSoftwareCombination(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<String> ->
            if (result.code == 200)
                mView?.postSoftwareCombination(result.data )
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}