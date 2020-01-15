package com.islam.otgtask.project_base.base.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.islam.basepropject.project_base.base.dialogs.BaseDialog
import com.islam.otgtask.R
import com.islam.otgtask.project_base.POJO.ErrorModel
import com.islam.otgtask.project_base.base.activities.BaseActivity
import com.islam.otgtask.project_base.base.other.BaseViewModel
import com.islam.otgtask.project_base.utils.ActivityManager
import com.islam.otgtask.project_base.utils.DialogManager
import com.islam.otgtask.project_base.utils.ImagePicker
import com.islam.otgtask.project_base.views.OnViewStatusChange

import java.io.File


abstract class BaseFragment<V : BaseViewModel> : Fragment(), DialogManager {


    lateinit var mViewModel: V

    private var mViewRoot: View? = null
    abstract var fragmentTag: String
    private var baseActivity: BaseActivity? = null
    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var savedInstanceState: Bundle? = null
    private val sensitiveInputViews = mutableListOf<View?>()


    //used to spicify this fragment should observe screen status or its children will take this responsibility
    private var hasChildrenFragments = false

    private var enableBackButton = false
    private var toolbarTitle = -1
    private var optionMenuId = -1
    private var layoutId = -1


    //override this method if you need to indetif another view group if the
    // loading full screen overlap on another view
    private val fullScreenViewGroup: ViewGroup
        get() = mViewRoot as ViewGroup

    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity!!.isNetworkConnected

    override val _fragmentManager: FragmentManager?
        get() = childFragmentManager
    override val _context: Context
        get() = context!!

    protected abstract fun onLaunch()

    protected abstract fun onViewCreated(view: View, viewModel: V, instance: Bundle?)

    protected abstract fun setUpObservers()

    protected fun onRetry() {
        mViewModel.loadInitialData()
    }

    protected fun addSensitiveInputs(vararg views: View?) {
        for (view in views) {
            sensitiveInputViews.add(view)
        }
    }

    protected fun enableSensitiveInputs(shouldEnable: Boolean) {
        for (view in sensitiveInputViews)
            view?.isEnabled = shouldEnable
    }

    protected fun initContentView(@LayoutRes layoutId: Int, hasChildrenFragments: Boolean = false) {
        this.layoutId = layoutId
        this.hasChildrenFragments = hasChildrenFragments
    }

    @JvmOverloads
    protected fun initToolbar(@StringRes toolbarTitle: Int, enableBackButton: Boolean = false, @MenuRes menuId: Int = -1) {
        this.enableBackButton = enableBackButton
        this.toolbarTitle = toolbarTitle
        optionMenuId = menuId
    }

    protected fun initViewModel(fragment: Fragment, viewModel: Class<V>) {
        this.mViewModel = ViewModelProvider(fragment).get(viewModel)
    }

    protected fun initViewModel(activity: FragmentActivity, viewModel: Class<V>) {
        this.mViewModel = ViewModelProvider(activity).get(viewModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.baseActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onLaunch()
        super.onCreate(savedInstanceState)
        checkValidResources()

        if (optionMenuId != -1)
            setHasOptionsMenu(true)
    }

    protected fun markScreenAsCompleted() {
        mViewModel.markAsCompleted(fragmentTag)
        tag
    }

    private fun checkValidResources() {
        if (layoutId == -1)
            throw IllegalArgumentException("you should call initContentView() method inside onLaunch Callback")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.savedInstanceState = savedInstanceState
        mViewRoot = inflater.inflate(layoutId, container, false)

        //register fragment so we can determine should we show full screen loading by consume screen status
        mViewModel.registerFragment(fragmentTag)

        if(savedInstanceState==null)
            mViewModel.loadInitialData()

        return mViewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()

        //only apply for the parent fragment
        if (parentFragment == null) {
            setUpToolbar()
            observeDefaults()
        }

        //used to spicify this fragment should observe screen status or its children will take this responsibility
        if (!hasChildrenFragments)
            observeScreenStatus()

        onViewCreated(view, mViewModel, savedInstanceState)
        setUpObservers()
    }


    private fun observeDefaults() {
        if (mViewModel == null) return

        mViewModel.mDialogMessage.observes(viewLifecycleOwner, Observer {
            showDialog(R.string.important,it)
        })

        //TODO need to be implemented
        mViewModel.mSnackBarMessage.observes(viewLifecycleOwner, Observer {})

        mViewModel.mDialogMessage.observes(viewLifecycleOwner, Observer {
            showDialog(R.string.title1, it)
        })

        mViewModel.mToastMessage.observes(viewLifecycleOwner, Observer { ActivityManager.showToastLong(context, it) })

        mViewModel.mEnableSensitiveInputs.observe(viewLifecycleOwner, Observer { enableSensitiveInputs(it) })

    }

    protected fun observeScreenStatus() {

        mViewModel.mShowLoadingFullScreen?.observe(viewLifecycleOwner, Observer {
            if (it.first != fragmentTag) return@Observer
            if (it.second) {
                inflateLoadingFullScreenView()
                ActivityManager.setVisibility(View.VISIBLE, mLoadingView)
            } else
                ActivityManager.setVisibility(View.GONE, mLoadingView)

        })

        mViewModel.mShowErrorFullScreen?.observe(viewLifecycleOwner, Observer {
            if (it.first != fragmentTag) return@Observer
            if (it.second != null) {
                inflateErrorFullScreenView(it.second!!)
                ActivityManager.setVisibility(View.VISIBLE, mErrorView)
            } else
                ActivityManager.setVisibility(View.GONE, mErrorView)
        })

        //TODO a better way to do this without needing to find view by id through all the list ( set list of this view in the fragment)
        mViewModel.mLoadingViews?.observe(viewLifecycleOwner, Observer {
            if (it.first != fragmentTag) return@Observer
            for (viewId in it.second)
                (mViewRoot?.findViewById<View>(viewId.key) as OnViewStatusChange).showLoading(viewId.value)
        })
    }

    //set fragment root view to be the dialog view so when searching for loading views we can find it
    fun exchangeRootViewToDialogView(view: View) {
        mViewRoot = view
    }

    //set fragment root view to be the dialog view so when searching for loading views we can find it
    fun exchangeRootViewToFragmentView() {
        mViewRoot = view
    }

    private fun inflateLoadingFullScreenView() {
        //   baseActivity!!.runOnUiThread {
        val viewGroup = fullScreenViewGroup
        if (mLoadingView != null) return
        mLoadingView = LayoutInflater.from(context).inflate(R.layout.layout_progress_bar,
                viewGroup, false)
        viewGroup.addView(mLoadingView)
        // }
    }

    private fun inflateErrorFullScreenView(errorModel: ErrorModel) {
        val viewGroup = fullScreenViewGroup
        if (mErrorView == null) {
            mErrorView = LayoutInflater.from(context).inflate(R.layout.layout_no_connection,
                    viewGroup, false)

            //to handel onRetry in each fragment individually
            mErrorView!!.findViewById<View>(R.id.btn_retry).setOnClickListener {
                if (isNetworkConnected)
                    onRetry()
            }

            viewGroup.addView(mErrorView)
        }
        (mErrorView!!.findViewById<View>(R.id.tv_title) as TextView).text = errorModel.title.getValue(context)
        (mErrorView!!.findViewById<View>(R.id.tv_message) as TextView).text = errorModel.message.getValue(context)
    }


    override fun onDestroyView() {
        if (mViewModel != null)
            mViewModel.unRegister(fragmentTag)
        sensitiveInputViews.clear()
        mViewRoot = null
        mLoadingView = null
        mErrorView = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(optionMenuId, menu)
    }

    override fun onDetach() {
        baseActivity = null
        mViewRoot = null
        super.onDetach()
    }

    protected fun finish() {
        activity?.finish()
    }

    fun setUpToolbar() {
        if (toolbarTitle != -1)
            baseActivity!!.setToolbarTitle(toolbarTitle)
        baseActivity?.enableBackButton(enableBackButton)
    }

    fun pickImage(onImagePicked: (imageFile: File?) -> Unit) {
        ImagePicker.pickImage(this, onImagePicked)
    }



    fun showDialog(dialog: BaseDialog){
        dialog.show(childFragmentManager)
    }

    fun toast(msg: String, lenght: Int = Toast.LENGTH_LONG) {
        ActivityManager.showToast(msg, lenght)
    }

    fun navigate(cls: Class<*>, bundle: Bundle? = null, clearBackStack: Boolean = false) {
        baseActivity?.navigate(cls, bundle, clearBackStack)
    }

    fun navigate(fragment: BaseFragment<*>, bundle: Bundle? = null,
                 @IdRes container: Int = R.id.container,
                 addToBackStack: Boolean = false,
                 isChildToThisFragment: Boolean = false) {
        val fragmentManager = if (isChildToThisFragment) childFragmentManager else activity?.supportFragmentManager
        baseActivity?.navigate(fragmentManager!!, fragment, bundle, container, addToBackStack)
    }


}


