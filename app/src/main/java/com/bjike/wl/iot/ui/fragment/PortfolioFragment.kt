package com.bjike.wl.iot.ui.fragment

import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import com.bjike.issp.mvp.contract.EquipmentContract

import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.mvp.contract.SoftwareContract
import com.bjike.issp.mvp.presenter.EquipmentPresenter
import kotlinx.android.synthetic.main.fragment_portfolio.*
import android.os.Vibrator
import android.provider.Settings
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.*
import com.bjike.wl.iot.ui.activtiy.DrawerActivity
import com.bjike.wl.iot.ui.activtiy.DrawerActivity.MyOnTouchListener
import android.widget.RelativeLayout
import com.bjike.wl.iot.adapter.MashupEquipmentListItemAdapter
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.mvp.contract.PortfolioContract
import com.bjike.wl.iot.mvp.presenter.PortfolioPresenter
import kotlinx.android.synthetic.main.fragment_portfolio.view.*
import kotlinx.android.synthetic.main.view_mashup_sensor_list_itme.view.*

/**
 * 传感器组合
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class PortfolioFragment : Fragment(), EquipmentContract.View, PortfolioContract.View {
    lateinit var view_layout: View
    private lateinit var list: MutableList<EquipmentBean>

    lateinit var presenter: EquipmentPresenter
    lateinit var portfolioPresenter: PortfolioPresenter

    lateinit var mVibrator: Vibrator
    var mWindowManager: WindowManager? = null
    var layoutParams: WindowManager.LayoutParams? = null
    private var tempPosition: Int = 0

    private val MODE_DRAG = 1
    private val MODE_NORMAL = 2

    private var mode = MODE_NORMAL

    lateinit var itemView: View
    private var dragView: View? = null
    // view的x差值
    private var mX: Float = 0.toFloat()
    // view的y差值
    private var mY: Float = 0.toFloat()
    // 手指按下时的x坐标(相对于整个屏幕)
    private var mWindowX: Float = 0.toFloat()
    // 手指按下时的y坐标(相对于整个屏幕)
    private var mWindowY: Float = 0.toFloat()


    lateinit var adapter: MashupEquipmentListItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view_layout = inflater!!.inflate(R.layout.fragment_portfolio, container, false)
        initData()
        initClick()
        return view_layout
    }

    fun initData() {
        view_layout.rv_control.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view_layout.rv_control.layoutManager = layoutManager
        list = ArrayList(0)
        adapter = MashupEquipmentListItemAdapter(activity)
        view_layout.rv_control.adapter = adapter


        mLayoutGroup = RelativeLayout(activity)
        mLayoutGroup!!.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color))

        mVibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        presenter = EquipmentPresenter(activity, this)
        portfolioPresenter = PortfolioPresenter(activity, this)

        presenter.getEquipmentData(HashMap(0))
        mWindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private fun initView() {

    }

    var position: Int = 0
    fun initClick() {

        var mGestureDetector: GestureDetector
        var onTouchListener = object : MyOnTouchListener {
            override fun onTouch(ev: MotionEvent): Boolean {
                mGestureDetector = GestureDetector(getActivity(), GestureDetector.SimpleOnGestureListener());
                mGestureDetector.onTouchEvent(ev);
                val eventAction = ev!!.action
                when (eventAction) {
                    MotionEvent.ACTION_DOWN -> {

                        mWindowX = ev.rawX
                        mWindowY = ev.rawY

                    }
                    MotionEvent.ACTION_MOVE -> {
                        updateWindow(ev)

                    }

                // 终止触摸时刻
                    MotionEvent.ACTION_UP -> {
                        if (mode == MODE_DRAG) {
                            closeWindow(ev.getX(), ev.getY())
                        }
                    }
                }

                return false;
            }
        }
        (activity as DrawerActivity).registerMyOnTouchListener(onTouchListener);
        view_layout.tv_post_portfolio.setOnClickListener {
            var fileMap = HashMap<String, String>(0)
            portfolioPresenter.postSoftwareCombination(fileMap)
        }
        view_layout.tv_clear.setOnClickListener {
            view_layout.rl_equipment.removeAllViews()
            adapter.setData(list)
        }
        adapter.setOnLongClickListener(object : MashupEquipmentListItemAdapter.OnItemLongClickListener {
            override fun onItemLongClick(Iview: View, position: Int) {
                isEquipment = false
                this@PortfolioFragment.position=position
                initLongClick(Iview)
            }

        })
    }

    var isEquipment: Boolean = false
    private fun initClick(Iview: View, position: Int) {

        Iview.setOnLongClickListener(View.OnLongClickListener {
            this@PortfolioFragment.position=position
            initLongClick(Iview)
            true
        })
    }

    fun initLongClick(Iview: View) {
        if (isEquipment) {
            view_layout.rl_equipment.removeView(Iview)
        } else {
            rv_control.removeView(Iview)
        }

        if (mode == MODE_DRAG) {
            false
        }
        itemView = Iview
        mX = Iview.width.toFloat() / 2
        mY = Iview.height.toFloat() / 2
        // 如果是Android 6.0 要动态申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(context)) {
                initWindow()
            } else {
                // 跳转到悬浮窗权限管理界面
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                context.startActivity(intent)
            }
        } else {
            // 如果小于Android 6.0 则直接执行
            initWindow()
        }
    }

    /**
     * 初始化window
     */
    private fun initWindow() {
        if (dragView == null) {
            dragView = View.inflate(activity, R.layout.view_mashup_sensor_list_itme, null)
            var iv = dragView!!.findViewById<ImageView>(R.id.iv_url)
            iv.setImageDrawable(itemView.iv_url.drawable)
            var tv_text = dragView!!.findViewById<TextView>(R.id.tv_name)
            tv_text.visibility = View.GONE
        }
        if (layoutParams == null) {
            layoutParams = WindowManager.LayoutParams()
            layoutParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
            layoutParams!!.format = PixelFormat.RGBA_8888
            layoutParams!!.gravity = Gravity.TOP or Gravity.LEFT
            layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等
            layoutParams!!.width = itemView.width
            layoutParams!!.height = itemView.height
            layoutParams!!.x = itemView.left + rl_equipment.getLeft()  //悬浮窗X的位置
            layoutParams!!.y = itemView.top + rl_equipment.getTop()  //悬浮窗Y的位置
           // itemView.visibility = View.INVISIBLE
        }

        mWindowManager!!.addView(dragView, layoutParams)
        mode = MODE_DRAG
    }

    /**
     * 触摸移动时，window更新
     *
     * @param ev
     */
    private fun updateWindow(ev: MotionEvent) {
        if (mode == MODE_DRAG) {
            val x = ev.rawX - mX
            val y = ev.rawY - mY
            if (layoutParams != null) {
                layoutParams!!.x = x.toInt()
                layoutParams!!.y = y.toInt()
                mWindowManager!!.updateViewLayout(dragView, layoutParams)
            }
        }
    }

    var mLayoutGroup: RelativeLayout? = null
    private var mPop: PopupWindow? = null

    internal var temp = intArrayOf(0, 0)


    /**
     * 关闭window
     *
     * @param x
     * @param y
     */
    private fun closeWindow(x: Float, y: Float) {
        if (dragView != null) {
            mWindowManager!!.removeView(dragView)
            dragView = null
            layoutParams = null
        }
        itemDrop(x, y)
        mode = MODE_NORMAL
    }

    fun itemDrop(x: Float, y: Float) {
        if (y > rl_equipment.top && y < rl_equipment.top + rl_equipment.height) {
            var bean = list[position]
            var mInflater = LayoutInflater.from(activity);
            val view = mInflater.inflate(R.layout.view_mashup_sensor_list_itme,
                    rv_control, false)
            var iv = view.findViewById<ImageView>(R.id.iv_url)
            iv.setImageDrawable(itemView.iv_url.drawable)
            val txt = view.findViewById<TextView>(R.id.tv_name)
            txt.visibility = View.GONE

            val lp = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            lp.leftMargin = (x - mX - rl_equipment.left).toInt()
            lp.topMargin = (y - mY * 2 - rl_equipment.top).toInt()


            if (isEquipment) {
                rl_equipment.removeView(itemView)
            } else {
               // adapter.remove(position)
                isEquipment = true
            }
            view.tag = position
            initClick(view, position)
            rl_equipment.addView(view, lp)


        } else {
            if (isEquipment) {
                rl_equipment.removeView(itemView)
                var bean = list[itemView.tag as Int]
              //  adapter.insert(bean, itemView.tag as Int)
            }
          //  itemView.visibility = View.VISIBLE
        }
    }

    private fun initPopWindow() {
        if (mPop == null) {

            mPop = PopupWindow(mLayoutGroup, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, true)

        }
        if (mPop!!.isShowing) {
            mPop!!.dismiss()
        }
    }


    override fun getEquipmentData(results: MutableList<EquipmentBean>) {
        list = results
        adapter.setData(results)
    }

    override fun postSoftwareCombination(results: String) {
        Toast.makeText(activity, results, Toast.LENGTH_LONG).show()
    }


    override fun showError(errorString: String) {
        if (errorString.isNullOrEmpty())
            return
        Toast.makeText(activity, errorString, Toast.LENGTH_LONG).show()
        Log.e("Portfolio", errorString)
    }

    companion object {
        private val ARG_PARAM = "param"

        fun newInstance(columnCount: Int): PortfolioFragment {
            val fragment = PortfolioFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM, columnCount)
            fragment.arguments = args
            return fragment
        }
    }

}
