package com.bjike.wl.iot.ui.activtiy

import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import com.bjike.wl.iot.R
import kotlinx.android.synthetic.main.activity_originality_details.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AbsListView
import com.bjike.issp.utils.showSoftInputFromWindow
import com.bjike.wl.iot.adapter.CommentListItemAdapter
import com.bjike.wl.iot.bean.CommentBean
import com.bjike.wl.iot.ui.view.DetailListView
import com.bjike.wl.iot.ui.view.DetailScrollView
import kotlinx.android.synthetic.main.view_custom_toolbar.*


class OriginalityDetailsActivity : AppCompatActivity() {
    lateinit var commentList: MutableList<CommentBean>
    lateinit var adapter: CommentListItemAdapter


    private var mListScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE
    private var mFirstVisibleItem: Int = 0
    private var mWebViewHeight: Int = 0
    private var mScreenHeight: Int = 0
    private var mLastY: Int = 0
    private var isMoving = false

    private val TAG = "OriginalityDetails"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_originality_details)
        initContentBodyView()
        initView()
        initData()
        initScrollView()
        initClick()
    }

    private fun initContentBodyView() {
        val vto = ll_content_body.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                ll_content_body.viewTreeObserver.removeGlobalOnLayoutListener(this)
                mWebViewHeight = ll_content_body.height
                scrollView.smoothScrollTo(0, mLastY)
            }
        })


    }

    fun initView() {
        commentList = ArrayList<CommentBean>(0)
        adapter = CommentListItemAdapter(commentList, this@OriginalityDetailsActivity)
        var layoutManager = LinearLayoutManager(this@OriginalityDetailsActivity, LinearLayoutManager.VERTICAL, false)
        rv_comment.layoutManager = layoutManager
        rv_comment.adapter = adapter
        rv_comment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, scrollState: Int) {
                super.onScrollStateChanged(recyclerView, scrollState)
                mListScrollState = scrollState

                //得到当前显示的最后一个item的view
                val lastChildView = recyclerView!!.layoutManager.getChildAt(recyclerView.layoutManager.childCount - 1)
                //得到lastChildView的bottom坐标值
                val lastChildBottom = lastChildView.bottom
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                val recyclerBottom = recyclerView!!.bottom - recyclerView.paddingBottom
                //通过这个lastChildView得到这个view当前的position值
                val lastPosition = recyclerView!!.getLayoutManager().getPosition(lastChildView)
                val last = recyclerView.layoutManager.itemCount - 1
                val top = recyclerView!!.canScrollVertically(1)
                val b = recyclerView!!.canScrollVertically(-1)
                //滚动到底部
                if (scrollState == SCROLL_STATE_IDLE && !recyclerView!!.canScrollVertically(1)) {
                    Log.d(TAG, "=======>listview 滚动到底部 加载更多,处理事件")

                    handleListViewTouchEvent()
                    return
                }
                //滚动到顶部
                if (scrollState == SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(-1)) {
                    Log.d(TAG, "=======>listview 滚动到顶部,不处理事件了,scrollview往上滚动5,让scrollview处理事件")
                    if (scrollView != null)
                        scrollView.smoothScrollBy(0, -5)
                    rv_comment.isHandleTouchEvent = false
                    return
                }
                //停止的时候,进行事件决定
                if (scrollState == SCROLL_STATE_IDLE) {
                    handleListViewTouchEvent()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mFirstVisibleItem = dy
                //Log.d(TAG,"onScroll firstVisibleItem:"+firstVisibleItem+"==>visibleItemCount:"+visibleItemCount+"==totalItemCount:"+totalItemCount);
            }
        })
        val height = getListViewHeight()
        rv_comment.getLayoutParams().height = height
        rv_comment.requestLayout()
        rv_comment.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "MyListView onTouch ACTION_DOWN")
            }
            if (event.action == MotionEvent.ACTION_MOVE) {
                Log.d(TAG, "MyListView onTouch ACTION_MOVE")
            }
            if (event.action == MotionEvent.ACTION_UP) {
                Log.d(TAG, "MyListView onTouch ACTION_UP")
            }
            //触摸的时候,让父控件不要拦截我的所有事件
            if (scrollView != null && rv_comment.isHandleTouchEvent) {
                if (event.action == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false)
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                }
            }
            false
        })
        rv_comment.moveListener = object : DetailListView.onMoveListener {
            override fun onMove(distance: Float) {
                //listview在顶部的时候,往下滑动,此时需要scrollview跟着一起滚动,进行过渡
                if (scrollView != null && rv_comment != null && rv_comment.getChildCount() > 0
                        && getListViewPositionAtScreen() >= getTopHeight() && distance > 0
                        && mFirstVisibleItem == 0 && rv_comment.getChildAt(0).getTop() === 0) {
                    // 滚动
                    scrollView.smoothScrollBy(0, -distance.toInt())
                    // 取消listview的事件消费,会在其onTouchEvent返回false
                    rv_comment.isHandleTouchEvent = false
                    isMoving = true
                }
            }

            //拖动scrollview一起滚动之后,在松开的时候,需要有个惯性滚动
            override fun onUp(velocityY: Int) {
                if (isMoving) {
                    Log.d(TAG, "mListView onUp:" + velocityY)
                    isMoving = false
                    //延迟一下fling才有效果。
                    Handler().postDelayed({
                        if (scrollView != null)
                            scrollView.fling(-velocityY)
                    }, 50)
                }
            }
        }
    }

    fun initData() {
        val label = ArrayList<String>(0)
        label.add("标签:")
        label.add("Android")
        label.add("IOS")
        label.add("前端")
        label.add("后台")
        label.add("微信开发")
        label.add("游戏开发")
        labels.setLabels(label) //直接设置一个字符串数组就可以了。
        lv_service_time.setLabels(label)
        lv_scene.setLabels(label)
        lv_crowd.setLabels(label)
        for (i in 0..20) {
            var bean = CommentBean()
            bean.userName = "name" + i
            bean.commetTime = "2017-02-27"
            commentList.add(bean)
        }

        adapter.setData(commentList)

    }

    private fun initClick() {
        lt_main_title_left.setOnClickListener { this@OriginalityDetailsActivity.finish() }
        ll_like.setOnClickListener {  }
        ll_stamp.setOnClickListener {  }
        ll_comment.setOnClickListener {  }
        et_comment.setOnClickListener { showSoftInputFromWindow(this@OriginalityDetailsActivity, et_comment) }
        adapter.setOnItemClickListener(onItemClickListener = object : CommentListItemAdapter.OnItemClickListener {
            override fun onItemClick(bean: CommentBean) {

            }

            override fun onCommentLikeClick(bean: CommentBean) {

            }

        })
    }

    private fun isWebViewOverScreen(): Boolean {
        return mWebViewHeight > mScreenHeight - getTopAndBottomHeight()
    }

    private fun initScrollView() {
        scrollView.childListView = rv_comment
        scrollView.setScanScrollChangedListener(object : DetailScrollView.ISmartScrollChangedListener {
            override fun onScrolledToBottom(vericalY: Int) {
                //到底的时候,事件交给listview,此时,需要让scrollview惯性滚动一下,没滚动完之前,不运行scrollview拦截
                if (!rv_comment.isHandleTouchEvent && vericalY < 0 && !scrollView.isTouchingScrollView) {
                    handleListViewTouchEvent()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rv_comment.fling(0, Math.abs(vericalY / 3))
                    } else {
                        rv_comment.startFling(Math.abs(vericalY / 3)/*5000*/)
                    }
                    rv_comment.isHandleTouchEvent = true
                    Log.d(TAG, "==》onScrolledToBottom 让listview fling了!!")
                    return
                }
                handleListViewTouchEvent()

            }

            override fun onScrolledToTop() {

            }
        })
        scrollView.moveListener = object : DetailScrollView.onMoveListener {
            //当按下scrollview的时候,如果listview还在fling,强制重置它的位置,并抢夺事件;
            override fun onDown() {

                if (rv_comment != null && mListScrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Log.d(TAG, "onDown listView还在fling,重置它的位置")
                    rv_comment.stopFling()
                    rv_comment.scrollToPosition(0)
                    rv_comment.isHandleTouchEvent = false
                }
            }

            //ListView过渡到ScrollView的时候,需要再惯性让ScrollView再滚动一下
            override fun onUp(velocityY: Int) {

                if (isMoving) {
                    Log.d(TAG, "mScrollView onUp:" + velocityY)
                    isMoving = false
                    if (scrollView != null && isWebViewOverScreen()) {
                        scrollView.fling(velocityY)
                    }
                }
                Log.d(TAG, "mScrollView onUp")
                handleListViewTouchEvent()
                //防止自带弹性效果的scrollview 计算不准确,再计算一次
                Handler().postDelayed({ handleListViewTouchEvent() }, 1000)

            }

            override fun onMove(distance: Float) {
                if (rv_comment != null && mListScrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Log.d(TAG, "onMove listView还在fling,重置它的位置")
                    rv_comment.stopFling()
                    rv_comment.scrollToPosition(0)
                    rv_comment.isHandleTouchEvent = false
                    mListScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                }
            }
        }
    }


    private fun getListViewHeight(): Int {
        return resources.displayMetrics.heightPixels - getTopAndBottomHeight()
    }

    private fun getTopHeight(): Int {
        return dip2px(applicationContext, (50 + 25 + 30).toFloat())
        //状态栏+标题栏
    }

    private fun getTopAndBottomHeight(): Int {
        return getTopHeight() + dip2px(applicationContext, 45f)
        //状态栏+标题栏+回复栏
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    private fun getListViewPositionAtScreen(): Int {
        try {
            val location = IntArray(2)
            rv_comment.getLocationInWindow(location)
            return location[1]
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return -1
    }

    //停止后,决定事件是由谁来处理的;
    private fun handleListViewTouchEvent() {
        try {
            if (rv_comment == null)
                return
            if (rv_comment.getChildCount() > 0 && getListViewPositionAtScreen() > getTopHeight()) {
                rv_comment.isHandleTouchEvent = false
                Log.d(TAG, "==》handleListViewTouchEvent listview 露出屏幕,由外部处理事件")
            } else {
                rv_comment.isHandleTouchEvent = true
                Log.d(TAG, "==》handleListViewTouchEvent listview 沾满全屏,自己处理事件")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}
