package com.bjike.wl.iot.bean

import android.os.Parcel
import android.os.Parcelable
import java.sql.Time

/**
 * 创意
 * author：T-Baymax on 2018/1/25 15:32
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class OriginalityBean: Parcelable {
    var id: String? = null
    var token: String? = null
    var username: String? = null
    var user_porstait: String? = null
    var originality_images: String? = null
    var content: String? = null
    var star_level: String? = null
    var time: String? = null
    var label: LabelBean? = null

    constructor(parcel: Parcel) {
        id = parcel.readString()
        token=parcel.readString()
        username=parcel.readString()
        user_porstait=parcel.readString()
        originality_images=parcel.readString()
        content=parcel.readString()
        star_level=parcel.readString()
        time=parcel.readString()
        label=parcel.readParcelable(LabelBean.javaClass.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(token)
        parcel.writeString(username)
        parcel.writeString(user_porstait)
        parcel.writeString(originality_images)
        parcel.writeString(content)
        parcel.writeString(star_level)
        parcel.writeString(time)
        parcel.writeParcelable(label,flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OriginalityBean> {
        override fun createFromParcel(parcel: Parcel): OriginalityBean {
            return OriginalityBean(parcel)
        }

        override fun newArray(size: Int): Array<OriginalityBean?> {
            return arrayOfNulls(size)
        }
    }
}