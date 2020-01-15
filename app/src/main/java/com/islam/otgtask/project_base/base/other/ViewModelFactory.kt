package com.islam.otgtask.project_base.base.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory private constructor() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ViewModel::class.java)) {

        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null

        val instance: ViewModelFactory
            get() {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = ViewModelFactory()
                        }
                        return INSTANCE as ViewModelFactory
                    }
                }
                return INSTANCE as ViewModelFactory
            }
    }

}
