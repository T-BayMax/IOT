package com.bjike.wl.iot.ui.activtiy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bjike.wl.iot.R
import com.bjike.wl.iot.bean.EquipmentBean
import com.bjike.wl.iot.bean.SoftwareBean
import kotlinx.android.synthetic.main.activity_equipment_details.*
import kotlinx.android.synthetic.main.view_custom_toolbar.*

/**
 * 传感器详情
 * author：T-Baymax on 2018/1/10 11:31
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentDetailsActivity : AppCompatActivity() {
    lateinit var equipment:EquipmentBean
    lateinit var software:SoftwareBean
    var position:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_details)
        initData()
        onClick()
    }
    fun initData(){
        position=intent.getIntExtra("position",0)
        if(position==0) {
            equipment=intent.getParcelableExtra("equipment")
            lt_main_title.text = equipment.sensor_key
            tv_name.text = "名称:" + equipment.name
            tv_describe.text = "特性:" + equipment.describe
            tv_scene.text = "用途:" + equipment.scene
        }
        if (position==1){
            software=intent.getParcelableExtra("software")
            tv_name.text = "名称:" + software.application_name
            tv_describe.text = "简介:" + software.application_content
        }

    }
    fun onClick(){
        lt_main_title_left.setOnClickListener(View.OnClickListener { this@EquipmentDetailsActivity.finish() })
    }
}
