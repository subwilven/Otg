package com.islam.otgtask.ui

import com.islam.otgtask.R
import com.islam.otgtask.project_base.POJO.NavigationType
import com.islam.otgtask.project_base.base.activities.BaseNavigationActivity
import com.islam.otgtask.ui.addContact.AddContactFramgent
import com.islam.otgtask.ui.contacts.ContactsFragment

class MainActivity : BaseNavigationActivity() {

    override val layoutId = R.layout.activity_main2

    override val navigationType = NavigationType.BottomNavigation

    override val menuIdsListWithFragment: MutableList<Pair<Int, Class<*>>>
        get() = mutableListOf<Pair<Int, Class<*>>>().run {
                add(Pair(R.id.navigation_home, ContactsFragment::class.java))
                add(Pair(R.id.navigation_dashboard, AddContactFramgent::class.java))
                this
            }

}

