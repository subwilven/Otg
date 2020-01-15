package com.islam.basepropject.project_base.base.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.islam.otgtask.project_base.base.fragments.BaseFragment
import com.islam.otgtask.project_base.base.other.BaseViewModel

abstract class BaseCustomDialog : BaseDialog() {

    protected open var toolbarTitle: Int = -1
    protected abstract var layoutId: Int
    protected open var cancelOnTouchOutside: Boolean = true
    protected open var isCancelablee: Boolean = true
    protected var mViewModel: BaseViewModel? = null
    var mView :View? =null


    abstract fun onDialogCreated(view: View?, savedInstanceState: Bundle?)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mViewModel = ((parentFragment as BaseFragment<*>).mViewModel)

        val dialog = MaterialDialog(context!!)
                .cancelOnTouchOutside(cancelOnTouchOutside)
                .cancelable(isCancelablee)
                .customView(layoutId)

        if (toolbarTitle != -1)
            dialog.title(toolbarTitle)
        mView =dialog.getCustomView()

        //set fragment root view to be the dialog view so when searching for loading views we can find it
        (parentFragment as BaseFragment<*>).exchangeRootViewToDialogView(mView!!)

        onDialogCreated(mView,savedInstanceState)

        return dialog
    }

    //set fragment root view to be the dialog view so when searching for loading views we can find it
    override fun onDismiss(dialog: DialogInterface) {
        (parentFragment as BaseFragment<*>).exchangeRootViewToFragmentView()
        super.onDismiss(dialog)
    }

}