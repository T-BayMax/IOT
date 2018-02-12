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
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.network.NetworkUrl
import com.bjike.wl.iot.utils.DensityUtil
import com.squareup.picasso.Picasso

/**
 * 传感器组合列表项
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class MashupEquipmentListItemAdapter(context: Context) : BaseRecyclerAdapter<MashupEquipmentListItemAdapter.SimpleAdapterViewHolder>() {
    private val largeCardHeight: Int
    private val smallCardHeight: Int
    private var itemList: MutableList<EquipmentBean> = ArrayList<EquipmentBean>(0)
    private var onItemClickListener: OnItemLongClickListener? = null

    init {
        largeCardHeight = DensityUtil.dip2px(context, 150f)
        smallCardHeight = DensityUtil.dip2px(context, 100f)
    }

    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {

        if (isItem) {
            var bean = itemList[position]
            var person = itemList[position]
            //  holder.nameTv.setText(person?.key)
            holder.ageTv.text = person.name
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                holder.rootView.layoutParams.height = if (position % 2 != 0) largeCardHeight else smallCardHeight
            }
            holder.rootView.tag = position
            holder.rootView.setOnLongClickListener(View.OnLongClickListener {
                onItemClickListener!!.onItemLongClick(holder.rootView, position)
                true
            })
            if (!bean.url.isNullOrBlank()) {
                Picasso.with(holder.itemView.context).load(NetworkUrl.BASE_URL + bean.url).into(holder.iv_url)

            }
        }
    }

    override fun getAdapterItemViewType(position: Int): Int {
        return 0
    }

    override fun getAdapterItemCount(): Int {
        return itemList.size
    }

    override fun getViewHolder(view: View): SimpleAdapterViewHolder {
        return SimpleAdapterViewHolder(view, false)
    }

    fun setData(list: MutableList<EquipmentBean>) {
        itemList.clear()
        this.itemList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.view_mashup_sensor_list_itme, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: EquipmentBean, position: Int) {
        itemList.add(position, person);
                notifyItemInserted(position);
        //insert(itemList!!, person, position)
    }

    fun remove(position: Int) {
        itemList.removeAt(position);
                 notifyItemRemoved(position);
        notifyItemRangeChanged(position, adapterItemCount);
      //  remove(itemList!!, position)
    }

    fun clear() {
        clear(itemList)
    }

    inner class SimpleAdapterViewHolder(itemView: View, isItem: Boolean) : RecyclerView.ViewHolder(itemView) {
        lateinit var rootView: View
        lateinit var iv_url: ImageView
        lateinit var ageTv: TextView

        init {

            if (isItem) {
                iv_url = itemView
                        .findViewById<ImageView>(R.id.iv_url)
                ageTv = itemView
                        .findViewById<TextView>(R.id.tv_name)
                rootView = itemView
                        .findViewById(R.id.ll_sensor)
            }

        }
    }

    fun getItem(position: Int): EquipmentBean? {
        return if (position < itemList.size)
            itemList[position]
        else
            null
    }

    open fun setOnLongClickListener(onItemClickListener: OnItemLongClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    open interface OnItemLongClickListener {
        open fun onItemLongClick(Iview: View, position: Int)
    }
}