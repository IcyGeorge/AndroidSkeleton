package com.orange.androidskeleton.repository.api

import com.orange.androidskeleton.vo.User
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

/**
 * REST API access points
 */
interface WebService {
    @GET("employees")
    fun getUsers(): Single<Response<List<User>>>
}
