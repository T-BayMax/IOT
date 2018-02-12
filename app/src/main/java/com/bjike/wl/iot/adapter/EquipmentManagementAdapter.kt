package com.bjike.wl.iot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView


import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.EquipmentBean

import java.util.ArrayList

/**
 * 我的组合列表项
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */

class EquipmentManagementAdapter(private val mContext: Context) : BaseExpandableListAdapter() {
    var data: MutableList<EquipmentBean> = ArrayList()
        private set
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }

    fun setDataset(dataset: MutableList<EquipmentBean>) {
        this.data = dataset
        notifyDataSetChanged()
    }

    //  获得某个父项的某个子项
    override fun getChild(parentPos: Int, childPos: Int): Any {
        return data[parentPos]
    }

    //  获得父项的数量
    override fun getGroupCount(): Int {
        return 1
    }

    //  获得某个父项的子项数目
    override fun getChildrenCount(parentPos: Int): Int {
        return data.size
    }

    //  获得某个父项
    override fun getGroup(parentPos: Int): Any {
        return data
    }

    //  获得某个父项的id
    override fun getGroupId(parentPos: Int): Long {
        return parentPos.toLong()
    }

    //  获得某个父项的某个子项的id
    override fun getChildId(parentPos: Int, childPos: Int): Long {
        return childPos.toLong()
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    override fun hasStableIds(): Boolean {
        return false
    }

    //  获得父项显示的view
    override fun getGroupView(parentPos: Int, b: Boolean, view: View?, viewGroup: ViewGroup): View {
        var groupView = view
        if (groupView == null) {
            groupView = inflater.inflate(R.layout.elv_parent_item, null)
        }
        val text = groupView!!.findViewById<View>(R.id.tv_parent_title) as TextView
        text.text = "广州"
        return groupView
    }

    //  获得子项显示的view
    override fun getChildView(parentPos: Int, childPos: Int, b: Boolean, view: View?, viewGroup: ViewGroup): View {
        var view = view
        var viewHolder: ViewHolder? = null
        val equiment = data[childPos]
        if (view == null) {
            view = inflater.inflate(R.layout.elv_child_item, null)
            viewHolder = ViewHolder()
            viewHolder.tv_name = view!!.findViewById<TextView>(R.id.tv_child_name)
            viewHolder.tv_status = view.findViewById<TextView>(R.id.tv_child_status)
            viewHolder.btn_open = view.findViewById<ImageView>(R.id.btn_equiment_status)
            // viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_child_delayTime);
            // viewHolder.followCheckBox = (CheckBox) view.findViewById(R.id.cb_child_check_item);

            view.tag = viewHolder
            viewHolder.btn_open!!.tag = data[childPos]
        } else {
            viewHolder = view.tag as ViewHolder
            //  viewHolder.btn_open.setTag(dataset.get(childPos));
        }

        viewHolder.tv_name!!.text = equiment.name
        /* if (null != equiment.getValue()) {
            viewHolder.tv_status.setBackgroundColor(equiment.getValue().equals("0") ? mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.green));
        }*/
        if (equiment.key != "") {//判断是否是假数据
            val finalViewHolder = viewHolder
            val finalViewHolder1 = viewHolder
            viewHolder.btn_open!!.setOnClickListener {
                val info = finalViewHolder.btn_open!!.tag as EquipmentBean
                info.isOpen = !info.isOpen
                //equiment.setOpen(!info.isOpen());
                notifyDataSetChanged()
                if (info.isOpen) {
                    finalViewHolder1.btn_open!!.setImageResource(R.mipmap.ab_on)
                } else {
                    finalViewHolder1.btn_open!!.setImageResource(R.mipmap.ab_off)
                }
            }
            // viewHolder.btn_open.setText(equiment.isOpen() ?   "关闭":"开启");
            val isOpen = (viewHolder.btn_open!!.tag as EquipmentBean).isOpen
            if (isOpen) {
                //  viewHolder.btn_open.setText("关闭");
                viewHolder.tv_status!!.text = equiment.value
                viewHolder.tv_status!!.setBackgroundColor(mContext.resources.getColor(android.R.color.holo_green_light))
            } else {
                //  viewHolder.btn_open.setText("开启");
                viewHolder.tv_status!!.text = "关闭"
                viewHolder.tv_status!!.setBackgroundColor(mContext.resources.getColor(R.color.col_E64242))
                // viewHolder.tv_status.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            }
        } else {
            //假数据处理
            // viewHolder.btn_open.setText("待开发");
            viewHolder.tv_status!!.text = ""
            // viewHolder.tv_status.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            //  viewHolder.btn_open.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }
        return view
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }

    private inner class ViewHolder {
        var tv_name: TextView? = null
        var tv_status: TextView? = null
        //  private TextView tv_time;
        // private CheckBox followCheckBox;
        var btn_open: ImageView? = null
    }
}