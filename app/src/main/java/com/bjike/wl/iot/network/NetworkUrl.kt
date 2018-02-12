package com.bjike.wl.iot.network

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class NetworkUrl {
    companion object {
        /*  val BASE_URL: String//服务器地址
              get() = "http://wl.bjike.com"*/
        val BASE_URL: String//本地服务器地址
            get() = "http://192.168.0.93:90"
        /*   val BASE_WS: String//websocket服务
               get() = "ws://144.host.bjike.com:8282"*/
        val BASE_WS: String//websocket服务
            get() = "ws://192.168.0.93:8282"
        val BASE_ORIGINAL: String//获取原图
            get() = BASE_URL + "/file/original/pic?path="
        val BASE_THUMBNAILS: String//获取缩略图
            get() = BASE_URL + "/file/original/pic?path="
        val BASE_DOWNLOAD: String//下载
            get() = BASE_URL + "/file/download?path="
    }
}