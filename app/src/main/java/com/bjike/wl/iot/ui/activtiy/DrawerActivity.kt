package com.bjike.wl.iot.ui.activtiy

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.internal.NavigationMenuPresenter
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.bjike.issp.utils.showToast
import com.bjike.wl.iot.App
import com.bjike.wl.iot.R
import com.bjike.wl.iot.service.ConnectService
import com.bjike.wl.iot.service.MyServiceConnection
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import android.view.MotionEvent
import android.widget.FrameLayout
import com.bjike.wl.iot.ui.fragment.*

/**
 * 创意
 * author：T-Baymax on 2018/1/10 10:31
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var tv_emile: TextView
    lateinit var tv_nickname: TextView
    lateinit var headerView: View
    lateinit var serviceConnection: MyServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        var intent = Intent(this, ConnectService::class.java)
        serviceConnection = MyServiceConnection()
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        /*  fab.setOnClickListener { view ->
              Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      .setAction("Action", null).show()
          }*/
        headerView = nav_view.getHeaderView(0)
        tv_emile = headerView.findViewById<TextView>(R.id.tv_emile)
        tv_nickname = headerView.findViewById<TextView>(R.id.tv_nickname)
        tv_emile.text = App.user
        tv_nickname.text = "T-BayMax"
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        getSupportActionBar()!!.title = getString(R.string.sensor_list)
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.itemTextColor = ContextCompat.getColorStateList(this, R.drawable.nav_menu_text_color);
        nav_view.itemIconTintList = ContextCompat.getColorStateList(this, R.drawable.nav_menu_text_color);
        nav_view.setItemBackgroundResource(R.color.equipment_bg)
        //nav_view.
        selectItem(0)//默认显示第一个
        setNavigationMenuLineStyle(nav_view, ContextCompat.getColor(this, android.R.color.white), 2)
        nav_view.setCheckedItem(R.id.nav_camera)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val it = Intent(this@DrawerActivity, OriginalityActivity::class.java)
                startActivity(it)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                selectItem(0)//传感器列表
                getSupportActionBar()!!.title = getString(R.string.sensor_list)
            }
            R.id.nav_gallery -> {
                selectItem(1)//软件技术列表
                getSupportActionBar()!!.title = getString(R.string.software_list)

            }
            R.id.nav_slideshow -> {
                selectItem(2)//传感器组合
                getSupportActionBar()!!.title = getString(R.string.combination_list)
            }
            R.id.nav_manage -> {
                selectItem(3)//混搭组合
                getSupportActionBar()!!.title = getString(R.string.mashup_list)
            }
            R.id.nav_my_equipment_management -> {
                selectItem(4)//我的组合
                getSupportActionBar()!!.title = getString(R.string.management_list)
            }
            R.id.nav_my_equipment_setting -> {
                selectItem(5)//我的组合设置
                getSupportActionBar()!!.title = getString(R.string.control_list)
            }
            R.id.nav_share -> {
                val it = Intent(this@DrawerActivity, OriginalityActivity::class.java)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
            R.id.nav_send -> {
                val it = Intent(this@DrawerActivity, CreativeSquareActivity::class.java)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
              /*  var map = HashMap<String, String>(0)
                map.put("type", "msg")
                map.put("receiveUser", "1270864354@qq.com")
                map.put("sendUser", App.user)
                map.put("data", "")
                map.put("msg", "发送失败请重试")
                var boo = serviceConnection.binder!!.sendTextMessage(Gson().toJson(map))
                if (!boo) {
                    showToast("发送失败请重试")
                }*/
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    var index: Int = -1
    fun selectItem(columnCount: Int) {
        if (columnCount == index) {
            return
        }
        index = columnCount
        when (columnCount) {
            0 -> {//传感器列表
                val fragment = EquipmentListFragment.Companion.newInstance(columnCount)
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment as Fragment).commit()
            }
            1 -> {//软件技术列表
                val fragment = SoftwareListFragment.Companion.newInstance(columnCount)
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment as Fragment).commit()
            }
            2 -> {//传感器组合
                val fragment = PortfolioFragment.Companion.newInstance(columnCount)
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment as Fragment).commit()
            }
            3 -> {

                val fragment = MashupFragment.Companion.newInstance(columnCount)
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment as Fragment).commit()
            }
            4 -> {//我的组合
                val fragment = EquipmentManagementFragment.Companion.newInstance(columnCount)
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment as Fragment).commit()
            }
            5 -> {//我的组合设置
                val fragment = EquipmentControlFragment.Companion.newInstance(columnCount)
                val fragmentManager = fragmentManager
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment as Fragment).commit()
            }
        }
    }

    interface MyOnTouchListener {
        fun onTouch(ev: MotionEvent): Boolean
    }

    private val onTouchListeners = ArrayList<MyOnTouchListener>(10)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        for (listener in onTouchListeners) {
            if (listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    fun registerMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    fun unregisterMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    fun setNavigationMenuLineStyle(navigationView: NavigationView, @ColorInt color: Int, height: Int) {
        try {
            var fieldByPressenter = navigationView.javaClass.getDeclaredField("mPresenter");
            fieldByPressenter.setAccessible(true);
            var menuPresenter = fieldByPressenter.get(navigationView) as NavigationMenuPresenter;
            var fieldByMenuView = menuPresenter.javaClass.getDeclaredField("mMenuView");
            fieldByMenuView.setAccessible(true);
            var mMenuView = fieldByMenuView.get(menuPresenter) as NavigationMenuView;
            mMenuView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewDetachedFromWindow(view: View?) {
                    var viewHolder = mMenuView.getChildViewHolder(view);
                    if (viewHolder != null && "SeparatorViewHolder".equals(viewHolder.javaClass.getSimpleName()) && viewHolder.itemView != null) {
                        if (viewHolder.itemView is FrameLayout) {
                            var frameLayout = viewHolder.itemView as FrameLayout;
                            var line = frameLayout.getChildAt(0);
                            line.setBackgroundColor(color);
                            line.layoutParams.height = height;
                            line.layoutParams = line.layoutParams;
                        }
                    }
                }

                override fun onChildViewAttachedToWindow(view: View?) {
                }
            });
        } catch (e: Throwable) {
            e.printStackTrace();
        }
    }
}
