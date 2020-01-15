package com.islam.otgtask.project_base.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

import com.google.android.material.button.MaterialButton
import com.islam.otgtask.R
import kotlin.math.max

//TODO need to register to to fragment/Activity lifecycle
class MyButton : MaterialButton, OnViewStatusChange {

    private var mProgressDrawable: CircularProgressDrawable? = null
    private var spannableString: SpannableString? =null
    private var mButtonText: String? =null
    private var mLoadingText: String? = ""
    private var mOnClickListener: View.OnClickListener? = null

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
                R.styleable.MyButton,
                0, 0)

        try {
            mLoadingText = a.getString(R.styleable.MyButton_loadingText)
            if (mLoadingText == null)
                mLoadingText = context.resources.getString(R.string.loading)
            mLoadingText += " "
        } finally {
            a.recycle()
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        mOnClickListener = onClickListener
        super.setOnClickListener(mOnClickListener)
    }

    private fun initDrawableSpan(): DynamicDrawableSpan {

        return object : DynamicDrawableSpan() {
            override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fontMetricsInt: Paint.FontMetricsInt?): Int {
                val rect = mProgressDrawable!!.bounds

                val fontMetrics = paint.fontMetrics

                // get a font height
                val lineHeight = fontMetrics.bottom - fontMetrics.top

                //make sure our drawable has height >= font height
                val drHeight = max(lineHeight, (rect.bottom - rect.top).toFloat())

                val centerY = fontMetrics.top + lineHeight / 2

                //adjust font metrics to fit our drawable size
                if (fontMetricsInt != null) {
                    fontMetricsInt.ascent = (centerY - drHeight / 2).toInt()
                    fontMetricsInt.descent = (centerY + drHeight / 2).toInt()
                    fontMetricsInt.top = fontMetricsInt.ascent
                    fontMetricsInt.bottom = fontMetricsInt.descent
                }

                //return drawable width which is in our case = drawable width + margin from text
                return rect.width() + 16
            }

            override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int,
                              paint: Paint) {
                canvas.save()
                val fontMetrics = paint.fontMetricsInt
                // get font height. in our case now it's drawable height
                val lineHeight = (fontMetrics.bottom - fontMetrics.top).toFloat()

                // adjust canvas vertically to draw drawable on text vertical center
                val centerY = y + fontMetrics.bottom - lineHeight / 2
                val transY = centerY - mProgressDrawable!!.bounds.height() / 2

                // adjust canvas horizontally to draw drawable with defined margin from text
                canvas.translate(x + 16, transY)

                mProgressDrawable!!.draw(canvas)

                canvas.restore()
            }

            override fun getDrawable(): Drawable? {
                return mProgressDrawable
            }
        }
    }

    private fun initProgressBar() {
        if (mProgressDrawable != null) return
        mProgressDrawable = CircularProgressDrawable(context)
        mProgressDrawable!!.setStyle(CircularProgressDrawable.DEFAULT)
        mProgressDrawable!!.setColorSchemeColors(Color.WHITE)
        val size = ((mProgressDrawable!!.centerRadius + mProgressDrawable!!.strokeWidth) * 2).toInt()
        mProgressDrawable!!.setBounds(0, 0, size, size)

        mProgressDrawable!!.callback = object : Drawable.Callback {
            override fun invalidateDrawable(who: Drawable) {
                this@MyButton.invalidate()
            }

            override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {

            }

            override fun unscheduleDrawable(who: Drawable, what: Runnable) {

            }
        }


        val dynamicDrawableSpan = initDrawableSpan()

        spannableString = SpannableString(mLoadingText)
        spannableString?.setSpan(dynamicDrawableSpan, spannableString?.length?.minus(1)!!,
                spannableString?.length!!, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }


    private fun startLoading() {
        super.setOnClickListener(null)
        initProgressBar()
        mButtonText = text.toString()
        text = spannableString
        mProgressDrawable?.start()


    }

    private fun stopLoading() {
        mProgressDrawable?.stop()
        mButtonText?.let { text = mButtonText}
        mOnClickListener?.let{ super.setOnClickListener(mOnClickListener)}
    }

    override fun showLoading(b: Boolean) {
        if (b) {
            startLoading()
        } else {
            stopLoading()
        }

    }


}
