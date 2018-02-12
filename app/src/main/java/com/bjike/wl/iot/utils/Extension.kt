package com.bjike.issp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.view.Display
import android.view.WindowManager
import android.util.DisplayMetrics
import android.widget.EditText



    /**
     * author：T-Baymax on 2017/11/20 11:11
     * mail：baixianhong_aj@163.com
     * authorization：bjike.com
     */
    fun Context.showToast(message: String): Toast {
        var toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
        return toast
    }

    fun showSoftInputFromWindow(activity: Activity, editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    inline fun <reified T : Activity> Activity.newIntent() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    fun <T> Observable<T>.applySchedulers(): Observable<T> {
        return subscribeOn(Schedulers.io()).
                unsubscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
    }


