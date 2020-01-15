package com.islam.otgtask.project_base.base.other.network

sealed class Result<out T>(val value:T?)

data class Success<out T>(val data: T) : Result<T> (data)

data class Failure(val error: String ="") : Result<Nothing>(null)