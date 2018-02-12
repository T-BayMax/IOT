package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.bean.ListContentBean
import com.bjike.wl.iot.bean.OriginalityBean
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.mvp.contract.OriginalityContract
import com.bjike.wl.iot.mvp.contract.SoftwareContract
import com.bjike.wl.iot.mvp.model.OriginalityModel
import com.bjike.wl.iot.mvp.model.SoftwareModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 * /
 */
class OriginalityPresenter
constructor(private val context: Context, view: OriginalityContract.View)
    : OriginalityContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: OriginalityContract.View? = null
    val mModel: OriginalityModel by lazy {
        OriginalityModel(context)
    }

    init {
        mView = view
        mContext = context
    }

    override fun getOriginalityListData(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<ListContentBean<MutableList<OriginalityBean>>>>? = mContext?.let { mModel.getOriginalityListData(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<ListContentBean<MutableList<OriginalityBean>>> ->
            if (result.code == 200)
                mView?.getOriginalityListData(result.data.content)
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}