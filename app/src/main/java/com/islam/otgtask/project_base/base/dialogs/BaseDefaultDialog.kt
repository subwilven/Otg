package com.islam.basepropject.project_base.base.dialogs

import android.app.Dialog
import android.os.Bundle

class BaseDefaultDialog(defaultDialog: Dialog) : BaseDialog() {

    var defaultDialog: Dialog? = defaultDialog


    init {
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return defaultDialog!!
    }

}