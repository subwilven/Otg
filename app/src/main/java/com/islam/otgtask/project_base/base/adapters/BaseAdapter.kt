package com.islam.otgtask.project_base.base.adapters

import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView

import com.islam.otgtask.project_base.views.MyRecyclerView

import java.util.ArrayList

abstract class BaseAdapter<T, VH : BaseViewHolder<T>> : RecyclerView.Adapter<VH> , LifecycleObserver {

    protected var list: MutableList<T>? = null

    constructor() {}

    constructor(list: MutableList<T>) {
        this.list = list
    }
    var adapterDataObservation: AdapterDataObservation?=null

    fun registerAdapterDataObservertion(lifecycleOwner: LifecycleOwner,recyclerView: MyRecyclerView) {
        lifecycleOwner.lifecycle.addObserver(this)
        adapterDataObservation = AdapterDataObservation(recyclerView)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        registerAdapterDataObserver(adapterDataObservation!!)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        unregisterAdapterDataObserver(adapterDataObservation!!)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clearAdapterDataObservation(){
        adapterDataObservation?.clear()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list!![position])
    }

    override fun getItemCount(): Int {
        return if (list != null) list!!.size else 0
    }

    fun setData(list: MutableList<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getItem(index :Int) : T?{
        list?.let {
            return it[index]
        }
        return null
    }

    fun addItem(item: T) {
        initList()
        list!!.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun addItem(item: T, position: Int) {
        initList()
        list!!.add(position, item)
        notifyItemInserted(position)
    }

    fun updateItem(item: T) {
        val index = list!!.indexOf(item)
        if (index != -1)
            updateItem(item, index)
    }

    fun updateItem(item: T, position: Int) {
        initList()
        list!![position] = item
        notifyItemChanged(position)
    }

    fun removeItem(item: T) {
        val index = list!!.indexOf(item)
        if (index != -1)
            removeItem(index)
    }

    fun removeItem(position: Int) {
        initList()
        list!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        initList()
        list!!.clear()
        notifyDataSetChanged()
    }

    private fun initList() {
        if (list == null)
            list = ArrayList()
    }
}
