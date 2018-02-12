package com.bjike.wl.iot.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter
import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.CreativeSquareBean
import com.bjike.wl.iot.bean.OriginalityBean
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.utils.DensityUtil

/**
 * 软件技术列表项
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CreativeSquareListItemAdapter(context: Context) : BaseRecyclerAdapter<CreativeSquareListItemAdapter.SimpleAdapterViewHolder>() {
    private var list: MutableList<CreativeSquareBean> = ArrayList<CreativeSquareBean>(0)
    private var onItemClickListener: OnItemClickListener? = null


    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {
        var bean = list[position]
        holder.iv_image
        holder.tv_title.text = bean.name
        holder.tv_content.text = bean.introduction
        holder.tv_user_name.text = bean.userName
        holder.tv_stamp.text=""+bean.stamp
        holder.iv_stamp
        holder.ll_stamp.setOnClickListener { onItemClickListener!!.onStampClick(bean) }
        holder.tv_like.text=""+bean.like
        holder.iv_like
        holder.ll_like.setOnClickListener { onItemClickListener!!.onLikeClick(bean) }
        holder.tv_address.text = bean.address
        holder.item_view.setOnClickListener { onItemClickListener!!.onItemClick(bean) }
    }

    override fun getAdapterItemViewType(position: Int): Int {
        return position
    }

    override fun getAdapterItemCount(): Int {
        return list.size
    }

    override fun getViewHolder(view: View): SimpleAdapterViewHolder {
        return SimpleAdapterViewHolder(view, false)
    }

    fun setData(list: MutableList<CreativeSquareBean>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.view_creative_square_item, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: CreativeSquareBean, position: Int) {
        insert(list, person, position)
    }

    fun remove(position: Int) {
        remove(list, position)
    }

    fun clear() {
        clear(list)
    }

    inner class SimpleAdapterViewHolder(itemView: View, isItem: Boolean) : RecyclerView.ViewHolder(itemView) {
        lateinit var item_view: View
        lateinit var iv_image: ImageView
        lateinit var tv_title: TextView
        lateinit var tv_content: TextView
        lateinit var tv_user_name: TextView
        lateinit var tv_stamp: TextView
        lateinit var iv_stamp: ImageView
        lateinit var ll_stamp: LinearLayout
        lateinit var tv_like: TextView
        lateinit var iv_like: ImageView
        lateinit var ll_like: LinearLayout
        lateinit var tv_address: TextView

        init {
            if (isItem) {
                iv_image = itemView.findViewById<ImageView>(R.id.iv_image)
                tv_title = itemView.findViewById<TextView>(R.id.tv_title)
                tv_content = itemView.findViewById<TextView>(R.id.tv_content)
                tv_user_name = itemView.findViewById<TextView>(R.id.tv_user_name)
                tv_stamp = itemView.findViewById<TextView>(R.id.tv_stamp)
                iv_stamp = itemView.findViewById<ImageView>(R.id.iv_stamp)
                ll_stamp = itemView.findViewById(R.id.ll_stamp)
                tv_like = itemView.findViewById<TextView>(R.id.tv_like)
                iv_like = itemView.findViewById<ImageView>(R.id.iv_like)
                ll_like = itemView.findViewById(R.id.ll_like)
                tv_address = itemView.findViewById<TextView>(R.id.tv_address)
                item_view = itemView.findViewById(R.id.item_view)
            }

        }
    }

    fun getItem(position: Int): CreativeSquareBean? {
        return if (position < list.size)
            list[position]
        else
            null
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onLikeClick(bean: CreativeSquareBean)
        fun onStampClick(bean: CreativeSquareBean)
        fun onItemClick(bean: CreativeSquareBean)
    }

}