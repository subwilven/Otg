package com.islam.otgtask.project_base.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.islam.otgtask.R


object ValidationManager {


    fun isValidEmail(target: CharSequence): Int {
        return if (TextUtils.isEmpty(target)) {
            R.string.all_fields_are_required
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            R.string.email_not_valid
        } else
            -1
    }

    fun isValidName(target: CharSequence): Int {
        return when {
            target.length < 3 -> R.string.name_should_be_at_least_3_charachter
            else -> -1
        }
    }


    fun isValidLongitude  (target: Double): Int {
        return when {
            target > 180 -> R.string.longitude_should_be_between
            target < -180 -> R.string.longitude_should_be_between
            else -> -1
        }
    }

    fun isValidLatitude (target: Double): Int {
        return when {
            target > 90 -> R.string.latitude_should_be_between
            target < -90 -> R.string.latitude_should_be_between
            else -> -1
        }
    }

    fun isValidPhone(target: CharSequence): Int {
        return when {
            target.length < 10 -> R.string.phone_not_valid
            else -> -1
        }
    }

    fun isValidPassword(target: CharSequence): Int {
        return when {
            TextUtils.isEmpty(target) -> R.string.all_fields_are_required
            target.length < 6 -> R.string.password_should_be_at_least_6_charachter
            else -> -1
        }
    }

}

open class DefaultTextWatcher(val block:(p0:String)-> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        block.invoke(p0.toString())
    }

}