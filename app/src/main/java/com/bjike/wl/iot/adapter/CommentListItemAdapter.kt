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
import com.bjike.wl.iot.bean.CommentBean
import com.bjike.wl.iot.bean.SoftwareBean
import com.bjike.wl.iot.network.NetworkUrl
import com.bjike.wl.iot.utils.CircleTransform
import com.bjike.wl.iot.utils.DensityUtil
import com.squareup.picasso.Picasso

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CommentListItemAdapter(private var list: List<CommentBean>?, context: Context) : BaseRecyclerAdapter<CommentListItemAdapter.SimpleAdapterViewHolder>() {
    private val largeCardHeight: Int
    private val smallCardHeight: Int
    private var onItemClickListener: OnItemClickListener? = null

    init {
        largeCardHeight = DensityUtil.dip2px(context, 150f)
        smallCardHeight = DensityUtil.dip2px(context, 100f)
    }

    override fun onBindViewHolder(holder: SimpleAdapterViewHolder, position: Int, isItem: Boolean) {
        var bean = list!![position]
        holder.tv_user_name.text = bean.userName
        holder.tv_commet_time.text = bean.commetTime
        holder.tv_comment_like.text = bean.commentLike
        holder.tv_comment_content.text = bean.commentContent
        if (position == list!!.size - 1) {
            holder.v_line.visibility = View.GONE
        } else {
            holder.v_line.visibility = View.VISIBLE
        }
        if (bean.ioc.isNullOrBlank()) {
            Picasso.with(holder.itemView.context).load(NetworkUrl.BASE_URL + bean.ioc).transform(CircleTransform()).into(holder.iv_ioc)
        }

        holder.ll_item.setOnClickListener { onItemClickListener!!.onItemClick(bean) }
        holder.ll_comment_like.setOnClickListener { onItemClickListener!!.onCommentLikeClick(bean) }

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

    fun setData(list: List<CommentBean>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, isItem: Boolean): SimpleAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.view_comment_list_item, parent, false)
        return SimpleAdapterViewHolder(v, true)
    }

    fun insert(person: CommentBean, position: Int) {
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
        lateinit var tv_user_name: TextView
        lateinit var tv_commet_time: TextView
        lateinit var tv_comment_like: TextView
        lateinit var tv_comment_content: TextView
        lateinit var ll_comment_like: LinearLayout
        lateinit var v_line: View
        lateinit var ll_item: LinearLayout


        init {
            if (isItem) {
                iv_ioc = itemView.findViewById<ImageView>(R.id.iv_ioc)
                tv_user_name = itemView.findViewById<TextView>(R.id.tv_user_name)
                tv_commet_time = itemView.findViewById<TextView>(R.id.tv_commet_time)
                tv_comment_like = itemView.findViewById<TextView>(R.id.tv_comment_like)
                tv_comment_content = itemView.findViewById<TextView>(R.id.tv_comment_content)
                ll_comment_like = itemView.findViewById(R.id.ll_comment_like)
                v_line = itemView.findViewById(R.id.v_line)
                ll_item = itemView.findViewById(R.id.ll_item)
            }

        }
    }

    fun getItem(position: Int): CommentBean? {
        return if (position < list!!.size)
            list!![position]
        else
            null
    }

     fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

     interface OnItemClickListener {
         fun onItemClick(bean: CommentBean)
         fun onCommentLikeClick(bean: CommentBean)
    }

}