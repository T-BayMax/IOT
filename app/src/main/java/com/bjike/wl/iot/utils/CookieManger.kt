package com.bjike.wl.iot.utils

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Cookie管理类
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CookieManger(context: Context) : CookieJar {

    init {
        mContext = context
        if (cookieStore == null) {
            cookieStore = PersistentCookieStore(mContext)
        }

    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>?) {
        if (cookies != null && cookies.size > 0) {
            for (item in cookies) {
                cookieStore!!.add(url, item)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore!![url]
    }

    internal class Customer(var userID: String?, var token: String?)

    companion object {


        var APP_PLATFORM = "app-platform"


        private lateinit var mContext: Context

        private var cookieStore: PersistentCookieStore? = null
    }
}
