package com.bjike.wl.iot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.EquipmentBean

import java.util.ArrayList

/**
 * 我的组合设置列表项
 *author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */

class EquipmentControlAdapter(private val mContext: Context) : BaseExpandableListAdapter() {
    var data: List<EquipmentBean> = ArrayList()

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }

    fun setDataset(data: List<EquipmentBean>) {
        this.data = data
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
        var itemView = view
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.elv_parent_item, null)
        }
        val text = itemView!!.findViewById<View>(R.id.tv_parent_title) as TextView
        text.text = "广州"
        return itemView


    }

    //  获得子项显示的view
    override fun getChildView(parentPos: Int, childPos: Int, b: Boolean, view: View?, viewGroup: ViewGroup): View {
        var view = view
        var viewHolder: ViewHolder?
        val equiment = data[childPos]
        if (view == null) {
            view = inflater.inflate(R.layout.elv_child_conteol_item, null)
            viewHolder = ViewHolder()
            viewHolder.tv_name = view!!.findViewById<View>(R.id.tv_child_name) as TextView
            viewHolder.et_time = view.findViewById<View>(R.id.et_child_delayTime) as EditText
            viewHolder.et_child_count = view.findViewById<View>(R.id.et_child_count) as EditText
            viewHolder.followCheckBox = view.findViewById<View>(R.id.cb_child_check_item) as CheckBox
            val finalViewHolder = viewHolder
            viewHolder.followCheckBox!!.setOnCheckedChangeListener { compoundButton, _ ->
                val info = finalViewHolder.followCheckBox!!.tag as EquipmentBean
                info.isCheck = compoundButton.isChecked
            }
            //            viewHolder.et_time.addTextChangedListener(new TextWatcher() {
            //                @Override
            //                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            //
            //                @Override
            //                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            //
            //                @Override
            //                public void afterTextChanged(Editable s) {
            //                    EquimentBean info = (EquimentBean) finalViewHolder.followCheckBox.getTag();
            //                    info.setDelayTime(s.toString());
            //                }
            //            });
            view.tag = viewHolder
            viewHolder.followCheckBox!!.tag = equiment
        } else {
            viewHolder = view.tag as ViewHolder
            viewHolder.followCheckBox!!.tag = equiment
        }
        //判断是否是假数据
        println("----key---" + equiment.key!!)
        if (equiment.key == "") {
            viewHolder.et_time!!.visibility = View.INVISIBLE
            viewHolder.followCheckBox!!.visibility = View.INVISIBLE
            viewHolder.et_child_count!!.visibility = View.INVISIBLE
        } else if (equiment.key == "Light_cover-003") {
            viewHolder.et_time!!.visibility = View.VISIBLE
            viewHolder.followCheckBox!!.visibility = View.VISIBLE
            viewHolder.et_child_count!!.visibility = View.VISIBLE
        }

        viewHolder.tv_name!!.text = equiment.name

        // viewHolder.et_time.setText(equiment.getDelayTime());

        viewHolder.followCheckBox!!.isChecked = equiment.isCheck


        return view
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }

    private inner class ViewHolder {
        var tv_name: TextView? = null
        var et_time: EditText? = null
        var et_child_count: EditText? = null
        var followCheckBox: CheckBox? = null
    }
}