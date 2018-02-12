package com.bjike.wl.iot.service

import android.app.AlertDialog
import android.app.Service
import android.content.*
import android.net.ConnectivityManager
import android.os.*
import android.util.Log
import android.widget.Toast

import com.bjike.wl.iot.App
import com.bjike.wl.iot.bean.Msg
import com.bjike.wl.iot.listener.WebSocketConnectionListener
import com.bjike.wl.iot.mvp.contract.WorkermanContract
import com.bjike.wl.iot.mvp.presenter.WorkermanPresenter
import com.bjike.wl.iot.ui.activtiy.DrawerActivity
import com.bjike.wl.iot.utils.MyUtils
import com.bjike.wl.iot.utils.NotificationUtils
import com.google.gson.Gson
import com.bjike.wl.iot.network.NetworkUrl
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.view.Display
import android.content.Context.WINDOW_SERVICE
import android.view.WindowManager
import android.content.DialogInterface
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.ACTION_WIRELESS_SETTINGS


/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */

class ConnectService : Service(), WorkermanContract.View {

    private val TAG = "ConnectService"
    private var isExit: Boolean = false//true 退出操作
    private var receiver: MyBroadcastReceiver? = null
    private var socketConnectionListener: WebSocketConnectionListener? = null
    var mVibrator: Vibrator? = null  //声明一个振动器对象


    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "绑定服务...")
        return MessageBinder()
    }

    override fun onCreate() {
        super.onCreate()

        WebSocketConnectionListener.setChatWebSocket(object : WebSocketConnectionListener.ChatDataObserver {
            override fun OnMessage(text: String) {
                //                msg.setFriendId(msg.getReceiver());
                val notification = NotificationUtils(this@ConnectService)
                val gson = Gson()
                val msg = gson.fromJson(text, Msg::class.java)
                if (msg.type == "init") {
                    /*  val retrofitClient = RetrofitClient.getInstance(this@ConnectService, NetworkUrl.BASE_URL)
                      val apiService = retrofitClient.create(EquipmentApi::class.java)

                      apiService!!.bindPost(map)*/
                    var presenter: WorkermanPresenter
                    presenter = WorkermanPresenter(this@ConnectService, this@ConnectService)
                    var map = HashMap<String, String>(0)
                    map.put("client_id", msg.data)
                    map.put("user", App.Companion.user)
                    presenter.bind(map)

                } else if (msg.type == "exit") {
                    notification.showNotification(msg)
                    WebSocketConnectionListener.removeSocket()
                    /*   Intent intent = new Intent(MyUtils.UpdateMessage);
                intent.putExtra("msg", msg);
                sendBroadcast(intent);*/
                    //                msg.setType(ChatRecyclerAdapter.FROM_USER_MSG);
                    //                msg.setTime(BaseChatActivity.returnTime());
                } else {
                   /* var message = Message()
                    message.obj = msg
                    handler.dispatchMessage(message)*/
                    showSystemDialog(msg.data)
                }
            }

            override fun onNetClose() {
                setmConnect()
            }
        })
    }

    lateinit var d: AlertDialog
    fun showSystemDialog(showInfo: String) {
        val b = AlertDialog.Builder(this@ConnectService)
        b.setMessage(showInfo)
        b.setPositiveButton("确定") { _, _ ->
            mVibrator!!.cancel()
            d.dismiss()
        }.setNegativeButton("取消") { _, _ -> d.dismiss() }
        d = b.create()
        d.window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        d.show()

        val lp = d.window.attributes
        val wm = this@ConnectService
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        if (display.height > display.width) {
            lp.width = (display.width * 1.0).toInt()
        } else {
            lp.width = (display.width * 0.5).toInt()
        }
        d.window.attributes = lp
    }

    var handler = Handler() { msg ->
        try {
            if (mVibrator == null) {
                mVibrator = application.getSystemService(VIBRATOR_SERVICE) as Vibrator
                mVibrator!!.vibrate(longArrayOf(1000, 10000, 1000, 10000), -1)
            }

            var m = msg.obj as Msg
            AlertDialog.Builder(this@ConnectService)
                    .setTitle("提示")
                    .setMessage(m.data)
                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                        mVibrator!!.cancel()
                    })
                    .show();
        } catch (e: Exception) {
            Log.e("", e.toString())
        }
        return@Handler true
    }

    override fun bind(results: String) {
        val intent = Intent(this@ConnectService, DrawerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent);
    }

    override fun showError(errorString: String) {
        Toast.makeText(this@ConnectService, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        connect()
        receiver = MyBroadcastReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(MyUtils.CONNECTIVITY_CHANGE)
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, intentFilter)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun connect() {
        /* if (null == App.userBean)
            return;*/
        val url = NetworkUrl.BASE_WS
        socketConnectionListener = WebSocketConnectionListener.getChartWebSocket(url)

    }

    inner class MessageBinder : Binder() {
        fun sendTextMessage(text: String): Boolean {
            if (App.client!!.followRedirects()) {
                if (!isExit) {

                    return socketConnectionListener!!.sendMessage(text)
                } else {
                    return false
                }
            } else {
                return false
            }
        }

        fun closeConnect() {
            disConnect()
        }
    }

    /**
     * 从新连接
     */
    fun setmConnect() {

        isExit = false
        connect()

    }

    fun disConnect() {

        isExit = true
        socketConnectionListener!!.closeWebSocket()
    }

    internal inner class MyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                disConnect()
                //JUtils.Toast("没有网络了");
            } else {
                setmConnect()
                // JUtils.Toast("网络恢复了");
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disConnect()
        unregisterReceiver(receiver)
    }
}
