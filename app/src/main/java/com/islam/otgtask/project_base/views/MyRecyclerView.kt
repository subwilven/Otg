package com.islam.otgtask.project_base.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.islam.otgtask.R
import com.islam.otgtask.project_base.utils.ActivityManager

class MyRecyclerView : ConstraintLayout, OnViewStatusChange {

    private var mLoadingView: View? = null
    private var mEmptyView: View? = null
    var adapter: Adapter<*>? = null
        set(adapter) {
            field = adapter
            recyclerView?.adapter = adapter
        }
    private var recyclerView: RecyclerView? = null
    private var emptyViewId = R.layout.layout_empty


    constructor(context: Context) : super(context) {
        recyclerView = RecyclerView(context)
        inflateRecyclerView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        recyclerView = RecyclerView(context, attrs)
        inflateRecyclerView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        recyclerView = RecyclerView(context, attrs, defStyleAttr)
        inflateRecyclerView()
    }


    fun setLayoutManager(layoutManager: LayoutManager) {
        recyclerView!!.layoutManager = layoutManager
    }

    fun setHasFixedSize(b: Boolean) {
        recyclerView!!.setHasFixedSize(b)
    }

    fun setEmptyView(emptyViewLayout: Int) {
        emptyViewId = emptyViewLayout
    }

    fun showEmptyView(b: Boolean) {

        if (b) {
            inflateEmptyView()
            ActivityManager.setVisibility(View.VISIBLE, mEmptyView)
        } else {
            ActivityManager.setVisibility(View.GONE, mEmptyView)
        }
    }

    fun showLoadingView(b: Boolean) {

        if (b) {
            ActivityManager.setVisibility(View.VISIBLE, mLoadingView)
        } else {
            inflateLoadingView()
            ActivityManager.setVisibility(View.GONE, mLoadingView)
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recyclerView?.adapter = null
        recyclerView = null
    }

    private fun inflateRecyclerView() {
        recyclerView?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        recyclerView?.id = View.generateViewId()
        addView(recyclerView)
    }

    private fun inflateLoadingView() {
        if (mLoadingView != null) return
        mLoadingView = LayoutInflater.from(context).inflate(R.layout.layout_progress_bar,
                this, false)
        addView(mLoadingView)
    }

    private fun inflateEmptyView() {
        if (mEmptyView != null) return
        mEmptyView = LayoutInflater.from(context).inflate(emptyViewId,
                this, false)
        addView(mEmptyView)
    }

    override fun showLoading(b: Boolean) {
        showLoadingView(b)
    }
}
