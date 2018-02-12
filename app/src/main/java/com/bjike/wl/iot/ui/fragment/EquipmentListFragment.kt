package com.bjike.wl.iot.ui.fragment

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.andview.refreshview.XRefreshView
import com.bjike.issp.mvp.contract.EquipmentContract
import com.bjike.issp.mvp.presenter.EquipmentPresenter

import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.EquipmentListItemAdapter
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.ui.activtiy.EquipmentDetailsActivity
import com.bjike.wl.iot.ui.view.CustomerFooter
import kotlinx.android.synthetic.main.fragment_equipmentlist_list.view.*
import java.util.ArrayList

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * 传感器列表
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class EquipmentListFragment : Fragment() , EquipmentContract.View {
    // TODO: Customize parameters
    private var mColumnCount = 1
   // private var mListener: OnListFragmentInteractionListener? = null
    internal lateinit var layoutManager: GridLayoutManager
    internal lateinit var view:View
    private lateinit var presenter: EquipmentPresenter


    internal lateinit var adapter: EquipmentListItemAdapter
    internal var personList: MutableList<EquipmentBean> = ArrayList<EquipmentBean>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         view = inflater!!.inflate(R.layout.fragment_equipmentlist_list, container, false)

        // Set the adapter
        initView()
        initData()
        initListener()
        return view
    }
    fun initView(){

        // xrefreshview.pullLoadEnable = true
        view.recycler_view_test_rv.setHasFixedSize(true)
        presenter = EquipmentPresenter(activity, this)

        adapter = EquipmentListItemAdapter(personList, activity)
        // 设置静默加载模式
//		xRefreshView1.setSilenceLoadMore();
        layoutManager = GridLayoutManager(activity, 4)
        view.recycler_view_test_rv.layoutManager = layoutManager
        // 静默加载模式不能设置footerview
        view.recycler_view_test_rv.adapter = adapter
//        xRefreshView1.setAutoLoadMore(false);
        view.xrefreshview.setPinnedTime(1000)
        view.xrefreshview.setMoveForHorizontal(true)

        //当需要使用数据不满一屏时不显示点击加载更多的效果时，解注释下面的三行代码
        //并注释掉第四行代码
        val customerFooter = CustomerFooter(activity)
        customerFooter.setRecyclerView(view.recycler_view_test_rv);
        adapter.setCustomLoadMoreView(customerFooter);
        // adapter.customLoadMoreView = XRefreshViewFooter(this)

        //   adapter.setCustomLoadMoreView( XRefreshViewFooter(this));
//		xRefreshView1.setPullLoadEnable(false);
        //设置静默加载时提前加载的item个数
//		xRefreshView1.setPreLoadCount(2);
    }
    fun initListener() {

        adapter.setOnItemClickListener(object : EquipmentListItemAdapter.OnItemClickListener {
            override fun onItemClick(bean: EquipmentBean) {
                val intent = Intent(activity, EquipmentDetailsActivity::class.java)
                intent.putExtra("equipment", bean);
                intent.putExtra("position",0)
                startActivity(intent)
            }
        }


        )
        view.xrefreshview.setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {

            override fun onRefresh(isPullDown: Boolean) {
                initData()
            }

            override fun onLoadMore(isSilence: Boolean) {
                initData()
            }
        })
    }
    private fun initData() {
        presenter.getEquipmentData(HashMap(0))

    }

    override fun getEquipmentData(results: MutableList<EquipmentBean>) {
        view.xrefreshview.stopRefresh(true)
        personList = results

        adapter.setData(personList)
    }

    override fun showError(eorrString: String) {
        view.xrefreshview.stopRefresh(false)
        Toast.makeText(activity, eorrString, Toast.LENGTH_LONG).show()
        Log.e("EquipmentList",eorrString)
    }
/*

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }
    */
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
  /*  interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem)
    }
*/
    companion object {

        // TODO: Customize parameter argument names
        val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): EquipmentListFragment {
            val fragment = EquipmentListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
