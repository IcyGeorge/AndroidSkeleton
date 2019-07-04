package com.orange.androidskeleton.repository.api

import com.orange.androidskeleton.vo.RedditPost
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface WebService {

    @GET("/r/{subreddit}/hot.json")
    fun getRedditPosts(@Path("subreddit") subreddit: String): Single<Response<ListingResponse>>

    companion object {
        const val BASE_URL = "https://www.reddit.com"
    }
}

