package com.bjike.wl.iot.ui.activtiy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Toast
import com.andview.refreshview.XRefreshView
import com.bjike.issp.mvp.contract.EquipmentContract
import com.bjike.issp.mvp.presenter.EquipmentPresenter
import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.EquipmentListItemAdapter
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.ui.view.CustomerFooter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*
import java.util.ArrayList

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class MainActivity : AppCompatActivity(), EquipmentContract.View {
    internal lateinit var adapter: EquipmentListItemAdapter
    internal var personList: MutableList<EquipmentBean> = ArrayList<EquipmentBean>(0)

    internal var lastVisibleItem = 0
    internal lateinit var layoutManager: GridLayoutManager
    private val isBottom = false
    private var mLoadCount = 0
    private lateinit var presenter: EquipmentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
        initListener()

    }
    fun initView(){
        lt_main_title_left.visibility= View.GONE
        lt_main_title.text="传感器"

        // xrefreshview.pullLoadEnable = true
        recycler_view_test_rv.setHasFixedSize(true)
        presenter = EquipmentPresenter(this, this)

        adapter = EquipmentListItemAdapter(personList, this)
        // 设置静默加载模式
//		xRefreshView1.setSilenceLoadMore();
        layoutManager = GridLayoutManager(this, 5)
        recycler_view_test_rv.layoutManager = layoutManager
        // 静默加载模式不能设置footerview
        recycler_view_test_rv.adapter = adapter
//        xRefreshView1.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000)
        xrefreshview.setMoveForHorizontal(true)

        //当需要使用数据不满一屏时不显示点击加载更多的效果时，解注释下面的三行代码
        //并注释掉第四行代码
        val customerFooter = CustomerFooter(this)
        customerFooter.setRecyclerView(recycler_view_test_rv);
        adapter.setCustomLoadMoreView(customerFooter);
        // adapter.customLoadMoreView = XRefreshViewFooter(this)

        //   adapter.setCustomLoadMoreView( XRefreshViewFooter(this));
//		xRefreshView1.setPullLoadEnable(false);
        //设置静默加载时提前加载的item个数
//		xRefreshView1.setPreLoadCount(2);
    }
    fun initListener(){

        adapter.setOnItemClickListener(object : EquipmentListItemAdapter.OnItemClickListener {
            override fun onItemClick(bean: EquipmentBean) {
                val intent = Intent(this@MainActivity, EquipmentDetailsActivity::class.java)
                intent.putExtra("equipment",bean);
                startActivity(intent)
            }
        }


        )
        xrefreshview.setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {

            override fun onRefresh(isPullDown: Boolean) {
                initData()
            }

            override fun onLoadMore(isSilence: Boolean) {
                initData()
            }
        })
//		// 实现Recyclerview的滚动监听，在这里可以自己处理到达底部加载更多的操作，可以不实现onLoadMore方法，更加自由
//		xRefreshView1.setOnRecyclerViewScrollListener(new OnScrollListener() {
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
//				lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//			}
//
//			public void onScrollStateChanged(RecyclerView recyclerView,
//											 int newState) {
//				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//					isBottom = recyclerviewAdapter.getItemCount() - 1 == lastVisibleItem;
//				}
//			}
//		});
    }

    private fun initData() {
        presenter.getEquipmentData(HashMap(0))

    }

    override fun getEquipmentData(results: MutableList<EquipmentBean>) {
        xrefreshview.stopRefresh(true)
        personList = results

        adapter.setData(personList)
    }

    override fun showError(eorrString: String) {
        xrefreshview.stopRefresh(false)
        Toast.makeText(this, eorrString, Toast.LENGTH_LONG).show()
    }


}
