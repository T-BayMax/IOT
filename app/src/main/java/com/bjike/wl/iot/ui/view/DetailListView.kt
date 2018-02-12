package com.bjike.wl.iot.ui.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.AbsListView
import android.widget.Scroller

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 用于详情评论列表滑动RecyclerView
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class DetailListView
//private ViewGroup mParentView;

(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private val listViewTouchAction: Int
    private val mScroller: Scroller?


    private var fLastRawY: Float = 0.toFloat()
    private var fMoveRawY: Float = 0.toFloat()
    private var isMoving = false
    private var mVelocityTracker: VelocityTracker? = null
    /**
     * 滑动监听器
     */
    var moveListener: onMoveListener? = null


    var isHandleTouchEvent = false

    private var mFlingRunnableField: Field? = null
    private var mFlingStart: Method? = null
    private var mFlingEnd: Method? = null
    private var reportScrollStateChange: Method? = null
    private var mFling: Method? = null

    init {
        mScroller = Scroller(context)
        listViewTouchAction = -1
        initFlingRunable()
        //setOnTouchListener(this);
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            //Log.d(TAG,"------->onTouchEvent MotionEvent:" + ev.getAction());
            if (mVelocityTracker == null) {
                //检查速度测量器，如果为null，获得一个
                mVelocityTracker = VelocityTracker.obtain()
            }
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "------->onTouchEvent ACTION_DOWN->y:" + ev.rawY)
                    fLastRawY = ev.rawY
                    isMoving = false
                    if (!mScroller!!.isFinished) { //fling
                        mScroller.abortAnimation()
                    }
                    mVelocityTracker!!.clear()
                    mVelocityTracker!!.addMovement(ev)
                    //break;
                    return super.onTouchEvent(ev)//为了列表项可以点击
                }
                MotionEvent.ACTION_MOVE -> {
                    mVelocityTracker!!.addMovement(ev)
                    fMoveRawY = ev.rawY
                    val fCount = fMoveRawY - fLastRawY
                    if (moveListener != null) {
                        moveListener!!.onMove(fCount)
                        isMoving = true
                        fLastRawY = fMoveRawY
                    }
                    Log.d(TAG, "------->onTouchEvent ACTION_MOVE  滑动距离:" + fCount + "==>getChildAt(0).getTop():" + getChildAt(0).top)
                }

                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "------->onTouchEvent ACTION_UP")
                    //mVelocityTracker.addMovement(ev);
                    val velocityTracker = mVelocityTracker
                    velocityTracker!!.computeCurrentVelocity(1000)
                    val initialVelocity = velocityTracker.getYVelocity(0).toInt()
                    Log.d(TAG, "------->速度是:" + initialVelocity)
                    if (moveListener != null && isMoving) {
                        moveListener!!.onUp(initialVelocity)
                        isMoving = false
                    }
                    //break;
                    return super.onTouchEvent(ev)//为了让按下效果可以消失
                }
            //为了让按下效果可以消失
                MotionEvent.ACTION_CANCEL -> {
                    Log.d(TAG, "------->onTouchEvent ACTION_CANCEL")
                    return super.onTouchEvent(ev)
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        //不处理事件了
        return if (!isHandleTouchEvent) {
            false
        } else super.onTouchEvent(ev)
    }


    private fun doFling(speed: Int) {
        if (mScroller == null) {
            return
        }
        mScroller.fling(0, scrollY, 0, speed, 0, 0, -500, 10000)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    interface onMoveListener {
        fun onMove(distance: Float)
        fun onUp(velocityY: Int)
    }

    //放于初始化方法中被调用
    fun initFlingRunable() {
        try {
            //if(mFlingEndField==null){
            mFlingRunnableField = AbsListView::class.java.getDeclaredField("mFlingRunnable")
            mFlingRunnableField!!.isAccessible = true
            mFlingStart = mFlingRunnableField!!.type.getDeclaredMethod("start", Int::class.javaPrimitiveType)//mFlingEndField.getType().getDeclaredMethod("start");
            mFlingStart!!.isAccessible = true

            mFlingEnd = mFlingRunnableField!!.type.getDeclaredMethod("endFling")//mFlingEndField.getType().getDeclaredMethod("start");
            mFlingEnd!!.isAccessible = true

            reportScrollStateChange = AbsListView::class.java.getDeclaredMethod("reportScrollStateChange", Int::class.javaPrimitiveType)
            reportScrollStateChange!!.isAccessible = true

            mFling = AbsListView::class.java.getDeclaredMethod("fling", Int::class.javaPrimitiveType)
            mFling!!.isAccessible = true
            //}
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun startFling(y: Int) {
        try {
            smoothScrollBy(10, 10)
            //mFlingStart.invoke(mFlingRunnableField.get(this));
            reportScrollStateChange!!.invoke(this, AbsListView.OnScrollListener.SCROLL_STATE_FLING)
            mFlingStart!!.invoke(mFlingRunnableField!!.get(this), y)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun stopFling() {
        try {
            if (mFlingEnd != null)
                mFlingEnd!!.invoke(mFlingRunnableField!!.get(this))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    companion object {

        private val TAG = "DetailListView"
        private val MAXIMUM_LIST_ITEMS_VIEWABLE = 10000
    }

}