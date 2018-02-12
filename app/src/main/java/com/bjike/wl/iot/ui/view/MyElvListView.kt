package com.bjike.wl.iot.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ExpandableListView

/**
 * Created by T-BayMax on 2017/3/13.
 */

class MyElvListView : ExpandableListView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    /* Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条 */
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}
