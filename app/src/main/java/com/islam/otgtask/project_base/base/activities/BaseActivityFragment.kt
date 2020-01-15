package com.islam.otgtask.project_base.base.activities

import android.os.Bundle
import com.islam.otgtask.R
import com.islam.otgtask.project_base.base.fragments.BaseFragment

abstract class BaseActivityFragment :BaseActivity(){

    override val layoutId = R.layout.activity_empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            navigate(supportFragmentManager, getStartFragment())
    }

    abstract fun getStartFragment(): BaseFragment<*>
}