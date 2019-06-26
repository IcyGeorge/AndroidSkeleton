package com.orange.androidskeleton.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.repository.api.WebService
import com.orange.androidskeleton.repository.db.UserDao
import com.orange.androidskeleton.util.RateLimiter
import com.orange.androidskeleton.vo.Resource
import com.orange.androidskeleton.vo.User
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val application: Application,
    private val userDao: UserDao,
    private val webService: WebService
) {

    private val usersListRateLimit = RateLimiter<Unit>(1, TimeUnit.MINUTES)

    fun loadUsers(): Flowable <Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<User>>(application) {
            override fun saveCallResult(item: List<User>) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return data == null || data.isEmpty() || usersListRateLimit.shouldFetch(Unit)
            }

            override fun loadFromDb() = userDao.loadUsers()

            override fun createCall() = webService.getUsers()

            override fun onFetchFailed() {
                usersListRateLimit.reset(Unit)
            }
        }.asFlowable()
    }
}
