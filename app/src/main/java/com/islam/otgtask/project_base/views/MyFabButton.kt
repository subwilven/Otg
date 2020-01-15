package com.islam.otgtask.project_base.views

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.islam.otgtask.R

class MyFabButton : FloatingActionButton, OnViewStatusChange {

    private var mProgressDrawable: Animatable? = null
    private var mImageDrawable: Drawable? = null
    private var mLoadingDrawable: Int = -1
    private var mOnClickListener: OnClickListener? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setUpCustomAttr(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setUpCustomAttr(attrs)
    }


    private fun setUpCustomAttr(attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MyImageView,
                0, 0)

        try {
            mLoadingDrawable = a.getInt(R.styleable.MyImageView_loadingDrawable, -1)
            if (mLoadingDrawable == -1)
                mLoadingDrawable = R.drawable.ic_circle_progress
        } finally {
            a.recycle()
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        mOnClickListener = onClickListener
        super.setOnClickListener(mOnClickListener)
    }

    override fun showLoading(b: Boolean) {
        if (b) {
            startLoading()
        } else {
            stopLoading()
        }

    }


    private fun startLoading() {
        super.setOnClickListener(null)
        mImageDrawable = drawable

        val animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(context, mLoadingDrawable)
        setImageDrawable(animatedVectorDrawableCompat)

        mProgressDrawable = drawable as Animatable
        if (!mProgressDrawable!!.isRunning)
            mProgressDrawable?.start()
    }


    private fun stopLoading() {
        mProgressDrawable?.stop()
        mImageDrawable?.let {setImageDrawable(mImageDrawable)}
        mOnClickListener?.let{ super.setOnClickListener(mOnClickListener)}
    }

}