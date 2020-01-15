package com.islam.otgtask.project_base.base.adapters

import androidx.recyclerview.widget.RecyclerView

import com.islam.otgtask.project_base.views.MyRecyclerView

class AdapterDataObservation(internal var recyclerView: MyRecyclerView?) : RecyclerView.AdapterDataObserver() {

    private fun checkAdapterHasData() {
        if (recyclerView?.adapter!!.itemCount == 0)
            recyclerView?.showEmptyView(true)
        else
            recyclerView?.showEmptyView(false)
    }

    fun clear(){
        recyclerView = null
    }

    override fun onChanged() {
        super.onChanged()
        checkAdapterHasData()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        checkAdapterHasData()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        checkAdapterHasData()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkAdapterHasData()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkAdapterHasData()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        checkAdapterHasData()
    }
}
