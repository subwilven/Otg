package com.islam.otgtask.project_base.base.other.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.islam.otgtask.R
import com.islam.otgtask.project_base.POJO.*
import com.islam.otgtask.project_base.base.other.BaseViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

open class RetrofitCorounites(private val baseViewModel: BaseViewModel) {

    private var viewId: Int? = null
    private var adapterItem: AdatperItemLoading? = null
    private lateinit var fragmentTag: String

    private val isStartingFragment: Boolean
        get() = baseViewModel.lastRegisterdFragmentStatus == ScreenStatus.STARTING

    constructor(baseViewModel: BaseViewModel,fragmentTag: String, viewId: Int?) : this(baseViewModel) {
        this.viewId = viewId
        this.fragmentTag = fragmentTag
    }

    constructor(baseViewModel: BaseViewModel,fragmentTag: String, adapterItem: AdatperItemLoading?) : this(baseViewModel) {
        this.adapterItem = adapterItem
        this.fragmentTag = fragmentTag
    }

    //handel errors like validation error or authentication error
    private fun getHttpErrorMessage(e: Throwable): String {
        return try {
            val error = (e as HttpException).response()?.errorBody()!!.string()
            val apiError = Gson().fromJson(error, ApiError::class.java)
            apiError.error.message
        } catch (e1: Exception) {
            e1.printStackTrace()
            "Cannot Handel Error Message"
        }
    }

    private fun handelNetworkError(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is CancellationException -> {}
            is HttpException ->
                onErrorReceived(getHttpErrorMessage(e))
            is SocketTimeoutException ->
                showError(Message(R.string.cannot_reach_the_server), ErrorModel.timeOut())
            is ConnectivityInterceptor.NoConnectivityException ->
                showError(Message(R.string.no_network_available), ErrorModel.noConnection())
            is JsonSyntaxException ->
                showError(Message(R.string.server_error))
            is IOException ->
                showError(Message(R.string.something_went_wrong))
            else ->
                showError(Message(R.string.unexpected_error_happened))
        }
    }

    open fun onErrorReceived(errorMsg: String) {
        showError(Message(errorMsg), ErrorModel.serverError(Message(errorMsg)))
    }


    private fun showError(msg: Message, errorModel: ErrorModel = ErrorModel.serverError(msg)) {

        if (isStartingFragment)
            baseViewModel.showNoConnectionFullScreen(fragmentTag,errorModel)
        else
            baseViewModel.showToastMessage(msg)
    }


    suspend fun <T> networkCall(block: suspend () -> T): Result<T> {
        return withContext(Dispatchers.Main) {
            try {
                baseViewModel.showLoading(fragmentTag,viewId,adapterItem)
                withContext(Dispatchers.IO) { Success(block.invoke()) }
            } catch (e: Throwable) {
                handelNetworkError(e)
                Failure()
            } finally {
                baseViewModel.hideLoading(fragmentTag,viewId,adapterItem)
            }
        }
    }

//    suspend fun <T> multipleNetworkCall(blocks: List<Pair<suspend () -> T,(T)->Unit>>): Result<Any> {
//        return withContext(Dispatchers.Main) {
//            try {
//                baseViewModel.showLoading(viewId)
//                val results :MutableList<Deferred<Any>> = mutableListOf()
//                blocks.forEach { results.add(async(Dispatchers.IO) { it.second(it.first.invoke()) })}
//                results.forEach { it.await()}
//                Success("")
//            } catch (e: Throwable) {
//                handelNetworkError(e)
//                Failure()
//            } finally {
//                baseViewModel.hideLoading(viewId)
//            }
//        }
//    }
}