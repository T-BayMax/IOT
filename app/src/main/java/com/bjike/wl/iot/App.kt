package com.bjike.wl.iot

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.StrictMode


import com.bjike.wl.iot.utils.MyUtils
//import com.bulong.rudeness.RudenessScreenHelper

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */

class App : Application() {

    var designWidth = 1080f

    override fun onCreate() {
        super.onCreate()
        // MultiDex.install(this)
       // RudenessScreenHelper(this, designWidth).activate();

        /*  NetChangeReceiver receiver = new NetChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyUtils.CONNECTIVITY_CHANGE);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);*/
        //声明日志类
        // HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        //设定日志级别
        //   httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(10000, TimeUnit.SECONDS)//设置连接超时时间

                //.addNetworkInterceptor(httpLoggingInterceptor)
                .build()
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure()
        }
        registerConnectivityNetworkMonitorForAPI21AndUp()
    }

    private fun registerConnectivityNetworkMonitorForAPI21AndUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(
                builder.build(),
                object : ConnectivityManager.NetworkCallback() {
                    /**
                     * @param network
                     */
                    override fun onAvailable(network: Network) {
                        //网络恢复
                        sendBroadcast(
                                getConnectivityIntent(false)
                        )

                    }

                    /**
                     * @param network
                     */
                    override fun onLost(network: Network) {
                        //网络关闭
                        sendBroadcast(
                                getConnectivityIntent(true)
                        )

                    }
                }

        )

    }

    /**
     * @param noConnection
     * @return
     */
    private fun getConnectivityIntent(noConnection: Boolean): Intent {
        val intent = Intent()
        intent.action = MyUtils.CONNECTIVITY_CHANGE
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, noConnection)
        return intent
    }

    companion object {
        //    private NetChangeReceiver receiver;

        var user = ""
        var token: String? = null
        var client: OkHttpClient? = null
    }
}

