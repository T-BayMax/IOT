package com.bjike.wl.iot.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * author：T-Baymax on 2018/2/1 15:12
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class CreativeSquareBean() : Parcelable {
    var name: String = ""
    var introduction: String = ""
    var like: Int = 0
    var stamp:Int=0
    var userName: String = ""
    var address: String = ""


    constructor(parcel: Parcel) : this() {
        name=parcel.readString()
        introduction=parcel.readString()
        like=parcel.readInt()
        stamp=parcel.readInt()
        userName=parcel.readString()
        address=parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(introduction)
        parcel.writeInt(like)
        parcel.writeInt(stamp)
        parcel.writeString(userName)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreativeSquareBean> {
        override fun createFromParcel(parcel: Parcel): CreativeSquareBean {
            return CreativeSquareBean(parcel)
        }

        override fun newArray(size: Int): Array<CreativeSquareBean?> {
            return arrayOfNulls(size)
        }
    }
}