package com.bjike.wl.iot.utils

import com.bjike.wl.iot.utils.GsonUtils.gson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.internal.ConstructorConstructor
import com.google.gson.internal.bind.CollectionTypeAdapterFactory
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * author：T-Baymax on 2017/11/20 11:11
 * mail：baixianhong_aj@163.com
 * authorization：bjike.com
 */
object GsonUtils {

    var gson: Gson

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    val STRING: TypeAdapter<String> = object : TypeAdapter<String>() {
        override fun read(reader: JsonReader): String {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return ""//原先是返回Null，这里改为返回空字符串
                }
                return reader.nextString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        override fun write(writer: JsonWriter, value: String?) {
            try {
                if (value == null) {
                    writer.nullValue()
                    return
                }
                writer.value(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 自定义adapter，解决由于数据类型为Int,实际传过来的值为Float，导致解析出错的问题
     * 目前的解决方案为将所有Int类型当成Double解析，再强制转换为Int
     */
    val INTEGER: TypeAdapter<Number> = object : TypeAdapter<Number>() {
        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Number {
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return 0
            }
            try {
                val i = `in`.nextDouble()
                return i.toInt()
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }

        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Number) {
            out.value(value)
        }
    }

    init {
        val gsonBulder = GsonBuilder()
        gsonBulder.registerTypeAdapter(String::class.java, STRING)   //所有String类型null替换为字符串“”
        gsonBulder.registerTypeAdapter(Int::class.javaPrimitiveType, INTEGER) //int类型对float做兼容

        //通过反射获取instanceCreators属性
        try {
            val builder = gsonBulder.javaClass as Class<*>
            val f = builder.getDeclaredField("instanceCreators")
            f.isAccessible = true
            val `val` = f.get(gsonBulder) as Map<Type, InstanceCreator<*>>//得到此属性的值
            //注册数组的处理器
            gsonBulder.registerTypeAdapterFactory(CollectionTypeAdapterFactory(ConstructorConstructor(`val`)))
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        gson = gsonBulder.create()
    }

    /**
     * Json字符串 转为指定对象
     *
     * @param json json字符串
     * @param type 对象类型
     * @param <T>  对象类型
     * @return
     * @throws JsonSyntaxException
    </T> */
    @Throws(JsonSyntaxException::class)
    fun <T> toBean(json: String, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

    /**
     *
     * @param obj
     * @return
     */
    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }
}
