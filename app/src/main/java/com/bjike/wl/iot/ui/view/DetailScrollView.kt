package com.bjike.wl.iot.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.ScrollView

/**
 * 用于详情评论列表滑动ScrollView
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class DetailScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {
    private var isScrolledToTop = true// 初始化的时候设置一下值
    private var isScrolledToBottom = false
    var childListView: DetailListView? = null
    private var mFlingVelocityY: Int = 0
    var isTouchingScrollView: Boolean = false
        private set

    private var mSmartScrollChangedListener: ISmartScrollChangedListener? = null

    private var fLastRawY: Float = 0.toFloat()
    private var fMoveRawY: Float = 0.toFloat()
    private var isMoving = false
    private var mVelocityTracker: VelocityTracker? = null


    var moveListener: onMoveListener? = null

    /** 定义监听接口  */
    interface ISmartScrollChangedListener {
        fun onScrolledToBottom(velocityY: Int)
        fun onScrolledToTop()
    }

    fun setScanScrollChangedListener(smartScrollChangedListener: ISmartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        //Log.d(TAG,"----------->onOverScrolled ");
        if (scrollY == 0) {
            isScrolledToTop = clampedY
            isScrolledToBottom = false
        } else {
            isScrolledToTop = false
            isScrolledToBottom = clampedY
        }
        //getSpeed();
        notifyScrollChangedListeners()
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //Log.d(TAG,"------->onScrollChanged ");
        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
            if (scrollY == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true
                isScrolledToBottom = false
            } else if (scrollY + height - paddingTop - paddingBottom == getChildAt(0).height) {
                // 小心踩坑2: 这里不能是 >=// 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
                isScrolledToBottom = true
                isScrolledToTop = false
            } else {
                isScrolledToTop = false
                isScrolledToBottom = false
            }
            notifyScrollChangedListeners()

        }
        //getSpeed();
        // 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
    }

    private fun notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener!!.onScrolledToTop()
            }
        } else if (isScrolledToBottom) {
            getSpeed()
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener!!.onScrolledToBottom(mFlingVelocityY)
            }
        }
    }

    override fun computeVerticalScrollExtent(): Int {
        return super.computeVerticalScrollExtent()
    }

    override fun computeVerticalScrollRange(): Int {
        return super.computeVerticalScrollRange()
    }

    override fun computeVerticalScrollOffset(): Int {
        return super.computeVerticalScrollOffset()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {

            if (mVelocityTracker == null) {
                //检查速度测量器，如果为null，获得一个
                mVelocityTracker = VelocityTracker.obtain()
            }
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    acquireVelocityTracker(ev)
                    Log.d(TAG, "------->onTouchEvent ACTION_DOWN->y:" + ev.rawY)
                    fLastRawY = ev.rawY
                    isMoving = false
                    isTouchingScrollView = true
                    if (moveListener != null) {
                        moveListener!!.onDown()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    isTouchingScrollView = true
                    acquireVelocityTracker(ev)
                    if (fLastRawY == 0f) {
                        fLastRawY = ev.rawY
                    }
                    fMoveRawY = ev.rawY
                    val fCount = fMoveRawY - fLastRawY
                    Log.d(TAG, "------->onTouchEvent ACTION_MOVE  滑动距离:" + fCount)
                    if (moveListener != null) {
                        moveListener!!.onMove(fCount)
                        isMoving = true
                        fLastRawY = fMoveRawY
                    }
                }
                MotionEvent.ACTION_UP -> {
                    isTouchingScrollView = false
                    acquireVelocityTracker(ev)
                    /*if(getChildListView()!=null){
                        getChildListView().onTouchUp(ev);
                    }*/
                    Log.d(TAG, "------->onTouchEvent ACTION_UP")
                    isMoving = false
                    fLastRawY = 0f
                    if (moveListener != null) {
                        getSpeed()
                        moveListener!!.onUp(mFlingVelocityY)
                    }
                    releaseVelocityTracker()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return super.onTouchEvent(ev)
    }


    private fun getSpeed() {
        try {
            if (mVelocityTracker != null) {
                val velocityTracker = mVelocityTracker
                velocityTracker!!.computeCurrentVelocity(1000)
                val initialVelocity = velocityTracker.getYVelocity(0).toInt()
                Log.d(TAG, "------->速度是:" + initialVelocity)
                mFlingVelocityY = initialVelocity
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    /**
     *
     * @param event 向VelocityTracker添加MotionEvent
     *
     * @see android.view.VelocityTracker.obtain
     * @see android.view.VelocityTracker.addMovement
     */
    private fun acquireVelocityTracker(event: MotionEvent) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }

    /**
     * 释放VelocityTracker
     *
     * @see android.view.VelocityTracker.clear
     * @see android.view.VelocityTracker.recycle
     */
    private fun releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            //mVelocityTracker.clear();
            //mVelocityTracker.recycle();
            //mVelocityTracker = null;
        }
    }

    interface onMoveListener {
        fun onDown()
        fun onUp(velocityY: Int)
        fun onMove(distance: Float)
    }

    companion object {

        private val TAG = "DetailScrollView"
    }

}