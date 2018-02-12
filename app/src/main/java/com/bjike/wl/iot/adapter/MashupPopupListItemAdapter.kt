package com.bjike.wl.iot.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter
import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.CreativeSquareBean
import com.bjike.wl.iot.network.NetworkUrl
import com.bjike.wl.iot.utils.CircleTransform
import com.bjike.wl.iot.utils.DensityUtil
import com.squareup.picasso.Picasso

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class MashupPopupListItemAdapter(private var list: List<CreativeSquareBean>?, context: Context) : BaseRecyclerAdapter<MashupPopupListItemAdapter.SimpleAdapterViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {
        var bean = list!![position]
        holder.tv_name.text = bean.userName
        holder.tv_content.text = bean.introduction

        if (bean.address.isNullOrBlank()) {
            Picasso.with(holder.itemView.context).load(NetworkUrl.BASE_URL + bean.address).transform(CircleTransform()).into(holder.iv_ioc)
        }

        holder.ll_item.setOnClickListener { onItemClickListener!!.onItemClick(bean) }


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

    fun setData(list: List<CreativeSquareBean>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.view_popu_list_item, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: CreativeSquareBean, position: Int) {
        insert(list!!, person, position)
    }

    fun remove(position: Int) {
        remove(list!!, position)
    }

    fun clear() {
        clear(list!!)
    }

    inner class SimpleAdapterViewHolder(itemView: View, isItem: Boolean) : RecyclerView.ViewHolder(itemView) {
        lateinit var iv_ioc: ImageView
        lateinit var tv_name: TextView
        lateinit var tv_content: TextView
        lateinit var ll_item: LinearLayout


        init {
            if (isItem) {
                iv_ioc = itemView.findViewById<ImageView>(R.id.iv_ioc)
                tv_name = itemView.findViewById<TextView>(R.id.tv_name)
                tv_content = itemView.findViewById<TextView>(R.id.tv_content)
                ll_item = itemView.findViewById(R.id.ll_item)
            }

        }
    }

    fun getItem(position: Int): CreativeSquareBean? {
        return if (position < list!!.size)
            list!![position]
        else
            null
    }

     fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

     interface OnItemClickListener {
         fun onItemClick(bean: CreativeSquareBean)
    }

}