package com.bjike.issp.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.contract.EquipmentContract
import com.bjike.issp.mvp.model.EquipmentModel
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.bean.EquipmentBean
import io.reactivex.Observable


/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentPresenter
constructor(private val context: Context, view: EquipmentContract.View)
    : EquipmentContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: EquipmentContract.View? = null
    val mModel: EquipmentModel by lazy {
        EquipmentModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun getEquipmentData(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<MutableList<EquipmentBean>>>? = mContext?.let { mModel.getEquipmentData(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<MutableList<EquipmentBean>> ->
            if (result.code == 200)
                mView?.getEquipmentData(result.data )
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}