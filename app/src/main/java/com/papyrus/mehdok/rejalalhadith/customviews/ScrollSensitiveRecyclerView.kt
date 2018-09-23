package com.papyrus.mehdok.rejalalhadith.customviews

import android.content.Context
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

open class ScrollSensitiveRecyclerView : RecyclerView {
    var uiToggleListener: UiToggleListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onViewScrolled(recyclerView, dx, dy)
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    open fun onViewScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            uiToggleListener?.hideUI()
        } else {
            uiToggleListener?.showUI()
        }
    }
}