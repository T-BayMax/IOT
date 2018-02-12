package com.bjike.wl.iot.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.EquipmentManagementAdapter
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.EquipmentManagementContract
import com.bjike.wl.iot.mvp.presenter.EquipmentManagementPresenter
import kotlinx.android.synthetic.main.fragment_item_list.view.*

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * 我的组合
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EquipmentManagementFragment : Fragment(), EquipmentManagementContract.View {
    lateinit var adapter: EquipmentManagementAdapter
    var itemList: MutableList<EquipmentBean>? = null
    lateinit var view_layout: View
    lateinit var presenter: EquipmentManagementPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view_layout = inflater!!.inflate(R.layout.fragment_item_list, container, false)
        initView()
        defaultList()
        return view_layout
    }

    private fun initView() {
        adapter = EquipmentManagementAdapter(this.activity)
        view_layout.lv_equipment_control.setAdapter(adapter)
        itemList = ArrayList<EquipmentBean>(0)
    }

    fun initData() {
        presenter.getEquipmentManagementData(HashMap<String, String>(0))
    }

    override fun getEquipmentManagementData(results: MutableList<EquipmentBean>) {

    }

    override fun showError(errorString: String) {
        Log.e("EquipmentManagement", errorString)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): EquipmentManagementFragment {
            val fragment = EquipmentManagementFragment()
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
