package com.bjike.issp.bean

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class JsonResult<T>(
        val code:Int,
        val msg:String,
        val data:T
) {
}