package com.bjike.wl.iot.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.EquipmentControlAdapter
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.EquipmentControlContract
import com.bjike.wl.iot.mvp.presenter.EquipmentControlPresenter
import kotlinx.android.synthetic.main.fragment_item_list.view.*

/**
 * 我的组合设置
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EquipmentControlFragment : Fragment(), EquipmentControlContract.View {
    lateinit var adapter: EquipmentControlAdapter
    var itemList: MutableList<EquipmentBean>? = null
    lateinit var view_layout: View
    lateinit var presenter: EquipmentControlPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view_layout = inflater!!.inflate(R.layout.fragment_item_list, container, false)
        initView()
        defaultList()
        return view_layout
    }

    private fun initView() {
        adapter = EquipmentControlAdapter(this.activity)
        view_layout.lv_equipment_control.setAdapter(adapter)
        itemList = ArrayList<EquipmentBean>(0)
    }

    fun initData() {
        presenter.getEquipmentControlData(HashMap<String, String>(0))
    }

    override fun getEquipmentControlData(results: MutableList<EquipmentBean>) {

    }

    override fun showError(errorString: String) {
        Log.e("EquipmentControl", errorString)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "equipment_control"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): EquipmentControlFragment {
            val fragment = EquipmentControlFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }

    private fun defaultList() {
        itemList?.add(EquipmentBean("磁簧开关", "", ""))
        itemList?.add(EquipmentBean("按键开关", "", ""))
        itemList?.add(EquipmentBean("水银开关", "", ""))
        itemList?.add(EquipmentBean("人体触摸", "", ""))
        itemList?.add(EquipmentBean("甲烷(MQ2)", "", ""))
        itemList?.add(EquipmentBean("酒精(MQ3)", "", ""))
        itemList?.add(EquipmentBean("光遮断", "", ""))
        itemList?.add(EquipmentBean("倾斜开关", "", ""))
        itemList?.add(EquipmentBean("咪头声音传感器", "", ""))
        itemList?.add(EquipmentBean("有源轰鸣器", "", ""))
        itemList?.add(EquipmentBean("继电器", "", ""))
        itemList?.add(EquipmentBean("DHT11温湿度", "", ""))
        itemList?.add(EquipmentBean("避障碍", "", ""))
        itemList?.add(EquipmentBean("迷你磁簧", "", ""))
        itemList?.add(EquipmentBean("激光", "", ""))
        itemList?.add(EquipmentBean("敲击", "", ""))
        itemList?.add(EquipmentBean("震动开关", "", ""))
        itemList?.add(EquipmentBean("红外发射", "", ""))
        itemList?.add(EquipmentBean("红外接收", "", ""))
        itemList?.add(EquipmentBean("循迹", "", ""))
        itemList?.add(EquipmentBean("舵机", "", ""))
        itemList?.add(EquipmentBean("超声波", "", ""))

        adapter.setDataset(itemList!!)

    }

}