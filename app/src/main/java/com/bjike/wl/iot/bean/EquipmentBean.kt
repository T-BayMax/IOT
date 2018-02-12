package com.bjike.wl.iot.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 传感器
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
class EquipmentBean : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(sensor_key)
        dest?.writeString(describe)
        dest?.writeString(url)
        dest?.writeString(scene)
        dest?.writeString(key)
        dest?.writeString(value)
        dest?.writeString(isCheck.toString())
        dest?.writeString(isOpen.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    var name: String? = null
    var sensor_key: String? = null
    var describe: String? = null
    var url: String? = null
    var scene: String? = null
    var key: String? = null
    var value: String? = null
    var isCheck = false
    var isOpen = true

    constructor(parcel: Parcel) {
        name = parcel.readString()
        sensor_key = parcel.readString()
        describe = parcel.readString()
        url = parcel.readString()
        scene = parcel.readString()
        key = parcel.readString()
        value = parcel.readString()
        isCheck = parcel.readString().toBoolean()
        isOpen = parcel.readString().toBoolean()
    }

    constructor(name: String, key: String, value: String) {
        this.name = name
        this.key = key
        this.value = value
    }

    companion object CREATOR : Parcelable.Creator<EquipmentBean> {
        override fun createFromParcel(parcel: Parcel): EquipmentBean {
            return EquipmentBean(parcel)
        }

        override fun newArray(size: Int): Array<EquipmentBean?> {
            return arrayOfNulls(size)
        }
    }
}
