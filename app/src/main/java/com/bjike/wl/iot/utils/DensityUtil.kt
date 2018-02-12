package com.bjike.wl.iot.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
object DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun  dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
    fun getWidth(context: Context):Int{
        val dm = context.resources.displayMetrics

        return dm.widthPixels;
    }

    /**
     * 获取屏幕分辨率宽度
     */
    fun getScreenWidth(activity:Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     *  获取屏幕分辨率高度
     */
    fun getScreenHeight(activity:Activity): Int {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }
}  
