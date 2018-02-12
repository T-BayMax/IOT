package com.bjike.wl.iot.ui.activtiy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.RelativeLayout
import android.widget.Toast
import com.andview.refreshview.XRefreshView
import com.bjike.issp.utils.showToast
import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.OriginalityListItemAdapter
import com.bjike.wl.iot.bean.OriginalityBean
import com.bjike.wl.iot.mvp.contract.OriginalityContract
import com.bjike.wl.iot.mvp.presenter.OriginalityPresenter
import com.bjike.wl.iot.ui.view.CustomerFooter
import com.bjike.wl.iot.utils.DensityUtil
import kotlinx.android.synthetic.main.activity_originality.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*

/**
 * 创意
 * author：T-Baymax on 2018/1/25 11:31
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class OriginalityActivity : AppCompatActivity(), OriginalityContract.View {
    lateinit var adapter: OriginalityListItemAdapter
    lateinit var list: MutableList<OriginalityBean>
    lateinit var presenter: OriginalityPresenter
    var page = 1
    private var isRefresh: Boolean = false  //是否为加载数据


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_originality)
        initView()
        initData()
        initClick()
    }

    private fun initView() {
        lt_main_title.text = "创意"
        showToast("t" + lt_main_title.textSize)
        lt_main_title_right.text = "筛选"
        lt_main_title_right.setCompoundDrawables(null, null, null, null)

        var layoutParams = lv_drawer_right.layoutParams
        layoutParams.width = DensityUtil.getScreenWidth(this@OriginalityActivity) * 2 / 3
        lv_drawer_right.layoutParams = layoutParams

        presenter = OriginalityPresenter(this@OriginalityActivity, this@OriginalityActivity)

        adapter = OriginalityListItemAdapter(this@OriginalityActivity)
        // 设置静默加载模式
        // xrefreshview.setSilenceLoadMore();
        var layoutManager = GridLayoutManager(this@OriginalityActivity, 2)
        rv_originality.layoutManager = layoutManager
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1500)
        xrefreshview.setMoveForHorizontal(true)
        rv_originality.setHasFixedSize(true)
        xrefreshview.pullLoadEnable = true

        rv_originality.adapter = adapter

        //当需要使用数据不满一屏时不显示点击加载更多的效果时，解注释下面的三行代码
        //并注释掉第四行代码
        val customerFooter = CustomerFooter(this@OriginalityActivity)
        customerFooter.setRecyclerView(rv_originality)
        adapter.customLoadMoreView = customerFooter
        //adapter.customLoadMoreView = XRefreshViewFooter(this)

        //设置静默加载时提前加载的item个数
//		xRefreshView1.setPreLoadCount(2);
    }

    private fun initData() {
        presenter.getOriginalityListData(HashMap(0))
    }

    private fun initClick() {
        lt_main_title_left.setOnClickListener { this@OriginalityActivity.finish() }
        xrefreshview.setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {

            override fun onRefresh(isPullDown: Boolean) {
                page = 1
                initData()
            }

            override fun onLoadMore(isSilence: Boolean) {
                page++
                initData()
            }
        })
        adapter.setOnItemClickListener(object : OriginalityListItemAdapter.OnItemClickListener {
            override fun onItemClick(bean: OriginalityBean) {
                var it = Intent(this@OriginalityActivity, OriginalityDetailsActivity::class.java)
                startActivity(it)
            }

            override fun onLikeClick(bean: OriginalityBean) {

            }

            override fun onStampClick(bean: OriginalityBean) {

            }

            override fun onCommentClick(bean: OriginalityBean) {

            }


        })
        lt_main_title_right.setOnClickListener { dl_layout.openDrawer(lv_drawer_right); }
        tv_empty.setOnClickListener { dl_layout.closeDrawer(lv_drawer_right) }
        tv_commit.setOnClickListener { dl_layout.closeDrawer(lv_drawer_right) }
    }

    override fun getOriginalityListData(results: MutableList<OriginalityBean>) {
        if (page == 1) {
            xrefreshview.stopRefresh(true)
        } else {
            xrefreshview.stopLoadMore(true)
        }
        list = results
        adapter.setData(results)
    }

    override fun showError(errorString: String) {
        if (page == 1) {
            xrefreshview.stopRefresh(false)
        } else {
            xrefreshview.stopLoadMore(false)
        }
        Toast.makeText(this@OriginalityActivity, errorString, Toast.LENGTH_LONG).show()
    }

}