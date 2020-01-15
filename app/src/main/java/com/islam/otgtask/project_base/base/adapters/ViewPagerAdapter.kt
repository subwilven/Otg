package com.islam.otgtask.project_base.base.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment, private val fragmentClasses: Array<Class<*>>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return fragmentClasses.size
    }

    override fun createFragment(position: Int): Fragment {
        try {
            return fragmentClasses[position].newInstance() as Fragment
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }

        return null!!
    }


}