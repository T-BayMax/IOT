package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.EquipmentManagementContract
import com.bjike.wl.iot.mvp.model.EquipmentManagementModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentManagementPresenter
constructor(private val context: Context, view: EquipmentManagementContract.View)
    : EquipmentManagementContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: EquipmentManagementContract.View? = null
    val mModel: EquipmentManagementModel by lazy {
        EquipmentManagementModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun getEquipmentManagementData(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<MutableList<EquipmentBean>>>? = mContext?.let { mModel.getEquipmentManagementData(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<MutableList<EquipmentBean>> ->
            if (result.code == 200)
                mView?.getEquipmentManagementData(result.data )
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}