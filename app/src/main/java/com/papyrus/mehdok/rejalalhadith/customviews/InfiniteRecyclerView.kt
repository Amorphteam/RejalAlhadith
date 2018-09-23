package com.papyrus.mehdok.rejalalhadith.customviews

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.papyrus.mehdok.rejalalhadith.R

class InfiniteRecyclerView : ScrollSensitiveRecyclerView {
    private var SCROLLAMOUNT = 3 / 4f
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    var infiniteScrollListener: InfiniteScrollListener? = null

    private var pageCounter: Int = 1

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getTriggerAmount(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        getTriggerAmount(context, attrs)
    }

    private fun getTriggerAmount(context: Context, attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.InfiniteRecyclerView)

        SCROLLAMOUNT = typedArray.getFloat(R.styleable.InfiniteRecyclerView_triggerAmount, SCROLLAMOUNT)

        typedArray.recycle()
    }

    override fun onViewScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onViewScrolled(recyclerView, dx, dy)

        // if user scroll upward don't ask for more content
        if (dy < 0) return

        visibleItemCount = layoutManager!!.childCount
        totalItemCount = layoutManager!!.itemCount
        pastVisibleItems = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (visibleItemCount + pastVisibleItems >= totalItemCount * SCROLLAMOUNT) {
            ++pageCounter
            infiniteScrollListener?.loadMoreContent(pageCounter)
        }
    }

    fun resetPageCounter() {
        pageCounter = 1
    }
}