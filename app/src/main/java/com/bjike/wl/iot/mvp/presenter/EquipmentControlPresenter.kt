package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.EquipmentControlContract
import com.bjike.wl.iot.mvp.model.EquipmentControlModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentControlPresenter
constructor(private val context: Context, view: EquipmentControlContract.View)
    : EquipmentControlContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: EquipmentControlContract.View? = null
    val mModel: EquipmentControlModel by lazy {
        EquipmentControlModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun getEquipmentControlData(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<MutableList<EquipmentBean>>>? = mContext?.let { mModel.getEquipmentControlData(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<MutableList<EquipmentBean>> ->
            if (result.code == 200)
                mView?.getEquipmentControlData(result.data )
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}