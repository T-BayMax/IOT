package com.bjike.wl.iot.ui.activtiy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.bjike.wl.iot.R
import com.bjike.wl.iot.adapter.CreativeSquareListItemAdapter
import com.bjike.wl.iot.bean.CreativeSquareBean
import com.bjike.wl.iot.mvp.contract.CreativeSquareContract
import com.bjike.wl.iot.mvp.presenter.CreativeSquarePresenter
import com.bjike.wl.iot.ui.view.CustomerFooter
import kotlinx.android.synthetic.main.activity_creative_square.*

/**
 * 创意广场
 * author：T-Baymax on 2018/1/10 10:31
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CreativeSquareActivity : AppCompatActivity(), CreativeSquareContract.View {
    lateinit var list: MutableList<CreativeSquareBean>
    lateinit var presenter: CreativeSquarePresenter
    lateinit var adapter: CreativeSquareListItemAdapter
    var page:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creative_square)
        initView()
        initData()
    }

    fun initView() {
        presenter = CreativeSquarePresenter(this@CreativeSquareActivity, this)
        list = ArrayList<CreativeSquareBean>(0)
        adapter = CreativeSquareListItemAdapter(this)
        rv_creative.adapter = adapter

        var layoutManager = GridLayoutManager(this@CreativeSquareActivity, 2)
        rv_creative.layoutManager = layoutManager
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1500)
        xrefreshview.setMoveForHorizontal(true)
        rv_creative.setHasFixedSize(true)
        xrefreshview.pullLoadEnable = true
        val customerFooter = CustomerFooter(this@CreativeSquareActivity)
        customerFooter.setRecyclerView(rv_creative)
        adapter.customLoadMoreView = customerFooter
    }

    fun initData() {
       // presenter.getCreativeData(HashMap<String, String>(0))
        for (i in 0..20) {
            list.add(CreativeSquareBean())
        }
        adapter.setData(list)
    }

    override fun getCreativeData(results: MutableList<CreativeSquareBean>) {
        list = results
        adapter.setData(results)
        if (page == 1) {
            xrefreshview.stopRefresh(true)
        } else {
            xrefreshview.stopLoadMore(true)
        }
    }

    override fun showError(errorString: String) {
        if (page == 1) {
            xrefreshview.stopRefresh(false)
        } else {
            xrefreshview.stopLoadMore(false)
        }
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show()
    }

}
