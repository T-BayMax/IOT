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
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.bean.OriginalityBean
import com.bjike.wl.iot.network.NetworkUrl
import com.bjike.wl.iot.ui.view.RatingBar
import com.bjike.wl.iot.utils.DensityUtil
import com.squareup.picasso.Picasso

/**
 * 创意列表项
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class OriginalityListItemAdapter(context: Context) : BaseRecyclerAdapter<OriginalityListItemAdapter.SimpleAdapterViewHolder>() {
    private var list: MutableList<OriginalityBean> = ArrayList<OriginalityBean>(0)
    private var onItemClickListener: OnItemClickListener? = null

    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {
        var bean = list[position]


        holder.rl_originality.setOnClickListener(View.OnClickListener { onItemClickListener!!.onItemClick(bean) })
        holder.ll_like.setOnClickListener(View.OnClickListener { onItemClickListener!!.onLikeClick(bean) })
        holder.ll_stamp.setOnClickListener(View.OnClickListener { onItemClickListener!!.onStampClick(bean) })
        holder.ll_comment.setOnClickListener(View.OnClickListener { onItemClickListener!!.onCommentClick(bean) })
        holder.rb_star.setStar(position % 5f)
        /* if (!bean.url.isNullOrBlank()) {
             Picasso.with(holder.itemView.context).load(NetworkUrl.BASE_URL + bean.url).into(holder.iv_png)

         }*/
    }

    override fun getAdapterItemViewType(position: Int): Int {
        return 0
    }

    override fun getAdapterItemCount(): Int {
        return list.size
    }

    override fun getViewHolder(view: View): SimpleAdapterViewHolder {
        return SimpleAdapterViewHolder(view, false)
    }

    fun setData(list: MutableList<OriginalityBean>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.view_originality_list_item, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: OriginalityBean, position: Int) {
        insert(list, person, position)
    }

    fun remove(position: Int) {
        remove(list, position)
    }

    fun clear() {
        clear(list)
    }

    inner class SimpleAdapterViewHolder(itemView: View, isItem: Boolean) : RecyclerView.ViewHolder(itemView) {
        lateinit var rl_originality: View
        lateinit var iv_images: ImageView
        lateinit var tv_porstait: TextView
        lateinit var tv_user_name: TextView
        lateinit var tv_time: TextView
        lateinit var tv_content: TextView

        lateinit var iv_like: ImageView
        lateinit var iv_stamp: ImageView
        lateinit var iv_comment: ImageView
        lateinit var tv_like: TextView
        lateinit var tv_stamp: TextView
        lateinit var tv_comment: TextView
        lateinit var ll_like: LinearLayout
        lateinit var ll_stamp: LinearLayout
        lateinit var ll_comment: LinearLayout
        lateinit var rb_star: RatingBar


        init {
            if (isItem) {
                rl_originality = itemView.findViewById(R.id.rl_originality)

                iv_images = itemView.findViewById<ImageView>(R.id.iv_images)
                tv_porstait = itemView.findViewById<TextView>(R.id.tv_porstait)
                tv_user_name = itemView.findViewById<TextView>(R.id.tv_user_name)
                tv_time = itemView.findViewById<TextView>(R.id.tv_time)
                tv_content = itemView.findViewById<TextView>(R.id.tv_content)

                iv_like = itemView.findViewById<ImageView>(R.id.iv_like)
                tv_like = itemView.findViewById<TextView>(R.id.tv_like)
                iv_stamp = itemView.findViewById<ImageView>(R.id.iv_stamp)
                tv_stamp = itemView.findViewById<TextView>(R.id.tv_stamp)
                iv_comment = itemView.findViewById<ImageView>(R.id.iv_comment)
                tv_comment = itemView.findViewById<TextView>(R.id.tv_comment)
                ll_like = itemView.findViewById(R.id.ll_like)
                ll_stamp = itemView.findViewById(R.id.ll_stamp)
                ll_comment = itemView.findViewById(R.id.ll_comment)

                rb_star = itemView.findViewById(R.id.rb_star)
            }

        }
    }

    fun getItem(position: Int): OriginalityBean? {
        return if (position < list.size)
            list[position]
        else
            null
    }

     fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

     interface OnItemClickListener {
         fun onItemClick(bean: OriginalityBean)
         fun onLikeClick(bean: OriginalityBean)
         fun onStampClick(bean: OriginalityBean)
         fun onCommentClick(bean: OriginalityBean)
    }
}