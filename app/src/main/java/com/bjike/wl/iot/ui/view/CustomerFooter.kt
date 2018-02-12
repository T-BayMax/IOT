package com.bjike.wl.iot.ui.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.andview.refreshview.XRefreshView
import com.andview.refreshview.callback.IFooterCallBack
import com.andview.refreshview.utils.Utils
/**
 * 列表底部加载更多
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CustomerFooter : LinearLayout, IFooterCallBack {
    private var mContext: Context? = null

    private var mContentView: View? = null
    private var mProgressBar: View? = null
    private var mHintView: TextView? = null
    private var mClickView: TextView? = null
    private var showing = false

    private var recyclerView: RecyclerView? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun callWhenNotAutoLoadMore(xRefreshView: XRefreshView) {
        if (recyclerView != null) {
            //当数据不满一屏时不显示点击加载更多
            show(Utils.isRecyclerViewFullscreen(recyclerView!!))
        }
        mClickView!!.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_click)
        mClickView!!.setOnClickListener { xRefreshView.notifyLoadMore() }
    }

    override fun onStateReady() {
        mHintView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.GONE
        mClickView!!.visibility = View.VISIBLE
        mClickView!!.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_click)
    }

    override fun onStateRefreshing() {
        mHintView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.VISIBLE
        mClickView!!.visibility = View.GONE
        show(true)
    }

    override fun onReleaseToLoadMore() {
        mHintView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.GONE
        mClickView!!.visibility = View.VISIBLE
        mClickView!!.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_release)
    }

    override fun onStateFinish(hideFooter: Boolean) {
        if (hideFooter) {
            mHintView!!.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_normal)
        } else {
            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
            mHintView!!.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_fail)
        }
        mHintView!!.visibility = View.VISIBLE
        mProgressBar!!.visibility = View.GONE
        mClickView!!.visibility = View.GONE
    }

    override fun onStateComplete() {
        mHintView!!.setText(com.andview.refreshview.R.string.xrefreshview_footer_hint_complete)
        mHintView!!.visibility = View.VISIBLE
        mProgressBar!!.visibility = View.GONE
        show(true)
    }

    override fun show(show: Boolean) {
        showing = show
        val lp = mContentView!!
                .layoutParams as LinearLayout.LayoutParams
        lp.height = if (show) LinearLayout.LayoutParams.WRAP_CONTENT else 0
        mContentView!!.layoutParams = lp
    }

    override fun isShowing(): Boolean {
        return showing
    }

    private fun initView(context: Context) {
        mContext = context
        val moreView = LayoutInflater.from(mContext).inflate(com.andview.refreshview.R.layout.xrefreshview_footer, this) as ViewGroup
        moreView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        mContentView = moreView.findViewById(com.andview.refreshview.R.id.xrefreshview_footer_content)
        mProgressBar = moreView
                .findViewById(com.andview.refreshview.R.id.xrefreshview_footer_progressbar)
        mHintView = moreView
                .findViewById<View>(com.andview.refreshview.R.id.xrefreshview_footer_hint_textview) as TextView
        mClickView = moreView
                .findViewById<View>(com.andview.refreshview.R.id.xrefreshview_footer_click_textview) as TextView
    }

    override fun getFooterHeight(): Int {
        return measuredHeight
    }
}
