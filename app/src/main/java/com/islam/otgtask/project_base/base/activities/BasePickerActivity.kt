package com.islam.otgtask.project_base.base.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Parcelable

import java.io.Serializable
import java.util.ArrayList

@SuppressLint("Registered")
abstract class BasePickerActivity : BaseActivity() {

    protected fun sendData(key: String, arrayList: ArrayList<*>) {
        val data = Intent()
        data.putExtra(key, arrayList)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    fun sendData(key: String, o: ArrayList<out Parcelable>, f: Boolean) {
        val data = Intent()
        data.putParcelableArrayListExtra(key, o)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    protected fun sendData(key: String, obj: Serializable) {
        val data = Intent()
        data.putExtra(key, obj)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    protected fun cancel() {
        val data = Intent()
        setResult(Activity.RESULT_CANCELED, data)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun onBackPressed() {
        cancel()
        super.onBackPressed()
    }
}
