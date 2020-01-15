package com.islam.otgtask.project_base.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.textfield.TextInputLayout
import com.islam.otgtask.MyApplication
import com.islam.otgtask.project_base.POJO.Message

object ActivityManager {

    fun setVisibility(v: Int, vararg views: View?) {
        for (view in views) {
            if (view != null)
                view.visibility = v
        }
    }


    fun showToastShort(context: Context?, message: Message) {
        showToast(message.getValue(context), Toast.LENGTH_SHORT)
    }

    fun showToastLong(context: Context?, message: Message) {
        showToast(message.getValue(context), Toast.LENGTH_LONG)
    }

    fun showToast(string: String, duration: Int) {
        Toast.makeText(MyApplication.instance?.applicationContext, string, duration).show()
    }

    fun ViewPager2?.nextPage() :Boolean {
        this?.let { viewPager ->
            if (viewPager.currentItem + 1 != viewPager.adapter?.itemCount!!){
                viewPager.currentItem = viewPager.currentItem.plus(1)
                return true
            }
        }
        return false
    }

    fun TextInputLayout.getText():String{
         return this.editText?.text.toString()
    }
}
