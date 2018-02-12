package com.bjike.wl.iot.utils

import android.graphics.*

import com.squareup.picasso.Transformation

/**
 * picasso绘制圆形图片
 * Created by T-BayMax on 2017/4/17.
 */

class CircleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()   //回收垃圾
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)//定义一个渲染器
        paint.shader = shader//设置渲染器
        paint.isAntiAlias = true//。。设置抗拒齿，图片边缘相对清楚

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)//绘制图形

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}
