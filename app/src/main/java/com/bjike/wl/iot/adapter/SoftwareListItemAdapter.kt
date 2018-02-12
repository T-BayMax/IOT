package com.bjike.wl.iot.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter
import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.utils.DensityUtil

/**
 * 软件技术列表项
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class SoftwareListItemAdapter(private var list: List<SoftwareBean>?, context: Context) : BaseRecyclerAdapter<SoftwareListItemAdapter.SimpleAdapterViewHolder>() {
    private val largeCardHeight: Int
    private val smallCardHeight: Int
    private var onItemClickListener : OnItemClickListener? = null

    init {
        largeCardHeight = DensityUtil.dip2px(context, 150f)
        smallCardHeight = DensityUtil.dip2px(context, 100f)
    }

    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {
        var bean= list!![position]
        var person = list?.get(position)
      //  holder.nameTv.setText(person?.key)
        holder.ageTv.setText(person?.application_name)
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            holder.rootView.layoutParams.height = if (position % 2 != 0) largeCardHeight else smallCardHeight
        }
        holder.rootView.setOnClickListener(View.OnClickListener { onItemClickListener!!.onItemClick(bean) })
        holder.rootView.setOnLongClickListener(View.OnLongClickListener {
            onItemLongClickListener!!.onItemLongClick(holder.rootView, position)
            true
        })
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

    fun setData(list: List<SoftwareBean>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.item_recylerview, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: SoftwareBean, position: Int) {
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


        init {
            if (isItem) {
                iv_png = itemView
                        .findViewById<ImageView>(R.id.iv_png)
                ageTv = itemView
                        .findViewById<TextView>(R.id.tv_name)
                rootView = itemView
                        .findViewById(R.id.card_view)
            }

        }
    }

    fun getItem(position: Int): SoftwareBean? {
        return if (position < list!!.size)
            list!![position]
        else
            null
    }

     fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
     interface  OnItemClickListener {
         fun onItemClick( bean: SoftwareBean)
    }
    private var onItemLongClickListener: OnItemLongClickListener? = null
     fun setOnLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener
    }

     interface OnItemLongClickListener {
         fun onItemLongClick(Iview: View, position: Int)
    }
}