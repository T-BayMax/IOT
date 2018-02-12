package com.bjike.wl.iot.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * author：T-Baymax on 2018/1/25 15:51
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class LabelBean: Parcelable {
    var id:String?=null
    var name:String?=null
    constructor(parcel: Parcel)  {
        id = parcel.readString()
        name=parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabelBean> {
        override fun createFromParcel(parcel: Parcel): LabelBean {
            return LabelBean(parcel)
        }

        override fun newArray(size: Int): Array<LabelBean?> {
            return arrayOfNulls(size)
        }
    }
}