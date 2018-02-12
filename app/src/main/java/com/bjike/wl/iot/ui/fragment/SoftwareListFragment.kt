package com.bjike.wl.iot.ui.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.andview.refreshview.XRefreshView
import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.SoftwareListItemAdapter
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.mvp.contract.SoftwareContract
import com.bjike.wl.iot.mvp.presenter.SoftwarePresenter
import com.bjike.wl.iot.ui.activtiy.EquipmentDetailsActivity
import com.bjike.wl.iot.ui.view.CustomerFooter
import kotlinx.android.synthetic.main.fragment_software.view.*
import java.util.ArrayList

/**
 * 软件技术列表
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class SoftwareListFragment : Fragment() , SoftwareContract.View {
    // TODO: Customize parameters
    private var mColumnCount = 1
    internal lateinit var layoutManager: GridLayoutManager
    internal lateinit var view: View
    private lateinit var presenter: SoftwarePresenter


    internal lateinit var adapter: SoftwareListItemAdapter
    internal var personList: MutableList<SoftwareBean> = ArrayList<SoftwareBean>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         view = inflater!!.inflate(R.layout.fragment_software, container, false)

        // Set the adapter
        initView()
        initData()
        initListener()
        return view
    }
    fun initView(){

        view.recycler_view_test_rv.setHasFixedSize(true)
        presenter = SoftwarePresenter(activity, this)

        adapter = SoftwareListItemAdapter(personList, activity)
        // 设置静默加载模式

        layoutManager = GridLayoutManager(activity, 5)
        view.recycler_view_test_rv.layoutManager = layoutManager
        // 静默加载模式不能设置footerview
        view.recycler_view_test_rv.adapter = adapter

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

        adapter.setOnItemClickListener(object : SoftwareListItemAdapter.OnItemClickListener {
            override fun onItemClick(bean: SoftwareBean) {
                val intent = Intent(activity, EquipmentDetailsActivity::class.java)
                intent.putExtra("software", bean);
                intent.putExtra("position",1)
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
        presenter.getSoftwareData(HashMap(0))

    }

    override fun getSoftwareData(results: MutableList<SoftwareBean>) {
        view.xrefreshview.stopRefresh(true)
        personList = results

        adapter.setData(personList)
    }

    override fun showError(eorrString: String) {
        view.xrefreshview.stopRefresh(false)
        Toast.makeText(activity, eorrString, Toast.LENGTH_LONG).show()
        Log.e("SoftwareList",eorrString)
    }

    companion object {

        // TODO: Customize parameter argument names
        val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(columnCount: Int): SoftwareListFragment {
            val fragment = SoftwareListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}