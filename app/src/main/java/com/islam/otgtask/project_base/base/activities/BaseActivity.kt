package com.islam.otgtask.project_base.base.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.islam.otgtask.project_base.base.fragments.BaseFragment
import com.islam.otgtask.MyApplication
import com.islam.otgtask.R
import com.islam.otgtask.project_base.common.boradcast.AlerterReceiver
import com.islam.otgtask.project_base.common.boradcast.ConnectivityReceiver
import com.islam.otgtask.project_base.utils.FragmentManagerUtil
import com.islam.otgtask.project_base.utils.LocalManager
import com.islam.otgtask.project_base.utils.NetworkManager
import com.tapadoo.alerter.Alerter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener, AlerterReceiver.AlerterReceiverListener {

    abstract val layoutId: Int

    val isNetworkConnected: Boolean
        get() = NetworkManager.isNetworkConnected(this)

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalManager.updateResources(newBase, LocalManager.getLanguage(newBase)))
    }


    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.setConnectivityListener(this)
        MyApplication.instance!!.setAlerterListener(this)
        updateConnectionState()
    }

    private fun checkValidResources() {
        if (layoutId == -1)
            throw IllegalArgumentException("you should call initContentView() method inside onLaunch Callback")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkValidResources()
        setContentView(layoutId)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onPause() {
        MyApplication.instance!!.setConnectivityListener(null)
        MyApplication.instance!!.setAlerterListener(null)
        super.onPause()
    }

    //called after user get back to activity after unActive state
    protected fun updateConnectionState() {
        onNetworkConnectionChanged(isNetworkConnected)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean?) {
        val tvConnectivityState = findViewById<TextView>(R.id.tv_connectivity_state)
        if (tvConnectivityState != null && isConnected != null) {
            if (isConnected) {
                lifecycleScope.launch {
                    tvConnectivityState.text = getString(R.string.connected)
                    tvConnectivityState.setBackgroundColor(resources.getColor(R.color.green))
                    tvConnectivityState.setTextColor(resources.getColor(R.color.green1))
                    withContext(Dispatchers.IO) { delay(2800) }
                    tvConnectivityState.visibility = View.GONE
                }
            } else {
                tvConnectivityState.text = getString(R.string.you_are_offline)
                tvConnectivityState.setBackgroundColor(resources.getColor(R.color.red))
                tvConnectivityState.setTextColor(resources.getColor(R.color.red1))
                tvConnectivityState.visibility = View.VISIBLE
            }
        }
    }


    override fun onAlertReceived(msg: String) {
        Alerter.create(this)
                .setTitle("Attention")
                .setText(msg)
                .enableSwipeToDismiss()
                .setDuration(4500)
                .setEnterAnimation(R.anim.alerter_slide_in_from_top)
                .setExitAnimation(R.anim.alerter_slide_out_to_top)
                .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun initToolbar() {
        if (supportActionBar == null) {
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)

            if (toolbar == null)
                throw IllegalStateException("No Toolbar included in the xml file with id \"toolbar\"")
        }
    }

    //always called from basefragment but if you have activity without any fragment feel free to call
    fun setToolbarTitle(@StringRes title: Int) {
        initToolbar()
        //search for textview with this id (in case this app want the title in the middle of the tool bar
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        if (toolbarTitle != null) {
            supportActionBar!!.title = ""
            toolbarTitle.setText(title)
        } else
        //in case the app title in the normal title in the toolbar
            supportActionBar!!.setTitle(title)
    }

    open fun enableBackButton(enableBackButton: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enableBackButton)
        supportActionBar?.setDisplayShowHomeEnabled(enableBackButton)
    }

    fun navigate(cls: Class<*>, bundle: Bundle? = null, clearBackStack: Boolean = false) {
        val intent = Intent(this, cls)
        if (clearBackStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
    }

    fun navigate(fragmentManager: FragmentManager,
                 fragment: BaseFragment<*>, bundle: Bundle? = null,
                 @IdRes container: Int = R.id.container,
                 addToBackStack: Boolean = false) {

        bundle?.let { fragment.arguments = (it) }

        FragmentManagerUtil.replaceFragment(fragmentManager,
                fragment,
                fragment.fragmentTag,
                setToBackStack = addToBackStack,
                containerId = container)
    }

    fun navigate(fragmentManager: FragmentManager,
                 fragment: Fragment,
                 tag: String,
                 bundle: Bundle? = null,
                 @IdRes container: Int = R.id.container,
                 addToBackStack: Boolean = false) {

        bundle?.let { fragment.arguments = (it) }

        FragmentManagerUtil.replaceFragment(fragmentManager,
                fragment,
                tag,
                setToBackStack = addToBackStack,
                containerId = container)
    }
}
