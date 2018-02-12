package com.bjike.wl.iot.ui.activtiy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bjike.wl.iot.R
import com.bjike.wl.iot.service.ConnectService

class LoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        val intent = Intent(this@LoadActivity, ConnectService::class.java)
        startService(intent)
    }
}
