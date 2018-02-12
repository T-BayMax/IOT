package com.bjike.wl.iot.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter
import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.network.NetworkUrl
import com.bjike.wl.iot.utils.DensityUtil
import com.squareup.picasso.Picasso
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Target


/**
 * 传感器列表项
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentListItemAdapter(private var list: List<EquipmentBean>?, context: Context) : BaseRecyclerAdapter<EquipmentListItemAdapter.SimpleAdapterViewHolder>() {
    private val largeCardHeight: Int
    private val smallCardHeight: Int
    private var onItemClickListener: OnItemClickListener? = null

    init {
        largeCardHeight = DensityUtil.dip2px(context, 150f)
        smallCardHeight = DensityUtil.dip2px(context, 100f)
    }

    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {
        var bean = list!![position]

        //  holder.nameTv.setText(person?.key);
        holder.ageTv.text = bean.name;
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.layoutParams.height = if (position % 2 != 0) largeCardHeight else smallCardHeight
        }
        holder.rootView.setOnClickListener(View.OnClickListener { onItemClickListener!!.onItemClick(bean) })
     /*   var width = DensityUtil.getWidth(holder.itemView.context)
        val params = holder.iv_png.getLayoutParams()

        params.height = width / 4//设置当前控件布局的高度
        params.width = width / 4
        holder.iv_png.setLayoutParams(params)//将设置好的布局参数应用到控件中
        holder.iv_png.scaleType = ImageView.ScaleType.FIT_CENTER*/
        /*holder.iv_png.adjustViewBounds=true*/


        if (!bean.url.isNullOrBlank()) {
            Picasso.with(holder.itemView.context).load(NetworkUrl.BASE_URL + bean.url).into(holder.iv_png)

        }
    }

    override fun getAdapterItemViewType(position: Int): Int {
        return 0
    }

    override fun getAdapterItemCount(): Int {
        return list!!.size
    }

    override fun getViewHolder(view: View): SimpleAdapterViewHolder {
        return SimpleAdapterViewHolder(view, false)
    }

    fun setData(list: List<EquipmentBean>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.item_recylerview, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: EquipmentBean, position: Int) {
        insert(list!!, person, position)
    }

    fun remove(position: Int) {
        remove(list!!, position)
    }

    fun clear() {
        clear(list!!)
    }

    inner class SimpleAdapterViewHolder(itemView: View, isItem: Boolean) : RecyclerView.ViewHolder(itemView) {
        lateinit var rootView: View
        lateinit var iv_png: ImageView
        lateinit var ageTv: TextView
        lateinit var r_view: LinearLayout


        init {
            if (isItem) {
                iv_png = itemView
                        .findViewById<ImageView>(R.id.iv_png)
                ageTv = itemView
                        .findViewById<TextView>(R.id.tv_name)
                rootView = itemView
                        .findViewById(R.id.card_view)
                r_view = itemView.findViewById(R.id.recycler_view_test_item_person_view)
            }

        }
    }

    fun getItem(position: Int): EquipmentBean? {
        return if (position < list!!.size)
            list!![position]
        else
            null
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(bean: EquipmentBean)
    }
}