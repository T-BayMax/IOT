package com.bjike.wl.iot.mvp.presenter

import android.content.Context
import com.bjike.issp.bean.JsonResult
import com.bjike.issp.mvp.presenter.BasePresenter
import com.bjike.issp.utils.applySchedulers
import com.bjike.wl.iot.bean.CreativeSquareBean
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.CreativeSquareContract
import com.bjike.wl.iot.mvp.contract.EquipmentControlContract
import com.bjike.wl.iot.mvp.model.CreativeSquareModel
import com.bjike.wl.iot.mvp.model.EquipmentControlModel
import io.reactivex.Observable

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CreativeSquarePresenter
constructor(private val context: Context, view: CreativeSquareContract.View)
    : CreativeSquareContract.Presenter, BasePresenter() {


    var mContext: Context? = null
    var mView: CreativeSquareContract.View? = null
    val mModel: CreativeSquareModel by lazy {
        CreativeSquareModel(context)
    }

    init {
        mView = view
        mContext = context
    }


    override fun getCreativeData(fieldMap: Map<String, String>) {
        val observable: Observable<JsonResult<MutableList<CreativeSquareBean>>>? = mContext?.let { mModel.getCreativeData(fieldMap) }
        observable?.applySchedulers()?.subscribe({ result: JsonResult<MutableList<CreativeSquareBean>> ->
            if (result.code == 200)
                mView?.getCreativeData(result.data )
            else
                mView?.showError(result.msg)
        }, { x -> mView?.showError(x.toString()) })
    }

}