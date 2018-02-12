package com.bjike.wl.iot.listener

import android.util.Log


import com.bjike.wl.iot.App
import com.bjike.wl.iot.bean.Msg
import com.google.gson.Gson

import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class WebSocketConnectionListener : WebSocketListener() {
    private var _WebSocket: WebSocket? = null
    private val msg: Msg? = null


    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        _WebSocket = webSocket
        Log.e("onOpen>>>>>>>", "open")
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        Log.e("MESSAGE>>>>>>>", text)

            observer.OnMessage(text!!);

    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        println("MESSAGE: " + bytes!!.hex())
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        /*  if (code==1000) {
            closeWebSocket();
        }else {*/
        observer.onNetClose()
        //        }
        Log.e("Close:", code.toString() + reason!!)
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        App.client!!.dispatcher().cancelAll()
        Log.e("onFailure>>>>>>", "onFailure")
        synchronized(this) {
            _WebSocket = null
        }
        t!!.printStackTrace()
    }

    /**
     * 初始化WebSocket服务器
     */
    private fun run(url: String) {
        val request = Request.Builder().url(url).build()
        App.client?.newWebSocket(request, this)
        //  App.client.dispatcher().executorService().shutdown();
    }

    /**
     * @param s
     * @return
     */
    fun sendMessage(s: String): Boolean {
        if (_WebSocket == null) {
            return false
        }else{
            return  _WebSocket!!.send(s)
        }

    }

    /**
     * @param s
     * @return
     */
    fun sendMessage(s: ByteString): Boolean {
        if (null!=_WebSocket){
            return _WebSocket!!.send(s)
        }else{
            return false
        }

    }

    fun closeWebSocket() {
        mChatWebSocket = null
        if (_WebSocket!!.close(1000, "主动关闭"))
            Log.e("close", "关闭成功")
    }

    interface ChatDataObserver {
        fun OnMessage(jsonObject: String)
        fun onNetClose()
    }

    companion object {

        private var mChatWebSocket: WebSocketConnectionListener? = null

        /**
         * 获取全局的ChatWebSocket类
         *
         * @return ChatWebSocket
         */
        fun getChartWebSocket(url: String): WebSocketConnectionListener {
            if (mChatWebSocket == null) {
                mChatWebSocket = WebSocketConnectionListener()
                mChatWebSocket!!.run(url)
            }
            return mChatWebSocket as WebSocketConnectionListener
        }

        internal lateinit var observer: ChatDataObserver

        fun removeSocket(){
            mChatWebSocket=null
        }
        fun setChatWebSocket(dataObserver: ChatDataObserver) {
            observer = dataObserver
        }
    }
}


