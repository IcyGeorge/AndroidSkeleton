package com.orange.androidskeleton.repository

import android.app.Application
import com.orange.androidskeleton.repository.api.ListingResponse
import com.orange.androidskeleton.repository.api.WebService
import com.orange.androidskeleton.repository.db.RedditPostDao
import com.orange.androidskeleton.util.RateLimiter
import com.orange.androidskeleton.vo.RedditPost
import com.orange.androidskeleton.vo.Resource
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditPostRepository @Inject constructor(
    private val application: Application,
    private val redditPostDao: RedditPostDao,
    private val webService: WebService
) {

    private val usersListRateLimit = RateLimiter<String>(1, TimeUnit.MINUTES)

    fun loadPosts(subreddit: String): Flowable <Resource<List<RedditPost>>> {
        return object : NetworkBoundResource<List<RedditPost>, ListingResponse>(application) {
            override fun saveCallResult(item: ListingResponse) {
                item.data.children.let { posts ->
                    val items = posts.map { child ->
                        child.data
                    }
                    redditPostDao.insert(items)
                }
            }

            override fun shouldFetch(data: List<RedditPost>?): Boolean {
                return data == null || data.isEmpty() || usersListRateLimit.shouldFetch(subreddit)
            }

            override fun loadFromDb() = redditPostDao.loadPosts(subreddit)

            override fun createCall() = webService.getRedditPosts(subreddit)

            override fun onFetchFailed() {
                usersListRateLimit.reset(subreddit)
            }
        }.asFlowable()
    }
}
