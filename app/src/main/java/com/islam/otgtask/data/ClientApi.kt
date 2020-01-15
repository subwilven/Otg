package com.islam.otgtask.data

import com.islam.otgtask.pojo.CountryCode
import com.islam.otgtask.project_base.POJO.ApiResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClientApi {

    @GET("all")
    suspend fun getCountryCodes(): List<CountryCode>
}
