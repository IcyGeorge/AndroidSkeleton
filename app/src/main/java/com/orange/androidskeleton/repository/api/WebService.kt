package com.orange.androidskeleton.repository.api

import androidx.lifecycle.LiveData
import com.orange.androidskeleton.vo.User
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface WebService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>
}
