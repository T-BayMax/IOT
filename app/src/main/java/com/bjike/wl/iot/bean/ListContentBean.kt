package com.bjike.wl.iot.bean

/**
 * 用于查询列表
 * author：T-Baymax on 2018/1/25 17:07
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class ListContentBean<T>(var page: Int,
                         var total: Int,
                         var content: T) {

}