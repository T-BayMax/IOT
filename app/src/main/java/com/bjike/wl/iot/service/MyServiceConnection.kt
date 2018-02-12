package com.bjike.wl.iot.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class MyServiceConnection : ServiceConnection {

    var binder: ConnectService.MessageBinder? = null

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        binder = service as ConnectService.MessageBinder   //该binder,需要在activity中声明。
        Log.d("learnservice", "绑定服务conn...")
    }

    override fun onServiceDisconnected(name: ComponentName) {
        Log.d("learnservice", "解除绑定服务conn...")
    }
}
