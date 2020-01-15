package com.islam.basepropject.project_base.base.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog : DialogFragment() {


    abstract override fun onCreateDialog(savedInstanceState: Bundle?): Dialog

    open fun show(fragmentManager: FragmentManager) {

        val tag = javaClass.name

        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(tag)
        show(transaction, tag)
    }

}

