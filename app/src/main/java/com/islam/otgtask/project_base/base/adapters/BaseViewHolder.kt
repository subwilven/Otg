package com.islam.otgtask.project_base.base.adapters

import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T>(viewGroup: ViewGroup, layoutId: Int) :
        RecyclerView.ViewHolder((LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false))),LayoutContainer {

     override val containerView: View?
         get() = itemView

     init {
        super.itemView.isClickable =true
        super.itemView.addRippleEffect()
    }

    abstract fun onBind(item: T)


    private fun View.addRippleEffect() = with(TypedValue()) {
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground = context.getDrawable(resourceId)
        }
    }
}