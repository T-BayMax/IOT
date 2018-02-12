package com.bjike.wl.iot.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 软件技术
 * author：T-Baymax on 2017/12/6 10:01
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class SoftwareBean() : Parcelable {
    var application_name: String? = null
    var application_content: String? = null

    constructor(parcel: Parcel) : this() {
        application_name = parcel.readString()
        application_content = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel?.writeString(application_name)
        parcel?.writeString(application_content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SoftwareBean> {
        override fun createFromParcel(parcel: Parcel): SoftwareBean {
            return SoftwareBean(parcel)
        }

        override fun newArray(size: Int): Array<SoftwareBean?> {
            return arrayOfNulls(size)
        }
    }
}