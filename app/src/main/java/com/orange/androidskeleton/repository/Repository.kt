package com.orange.androidskeleton.repository

import androidx.lifecycle.LiveData
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.repository.api.WebService
import com.orange.androidskeleton.repository.db.UserDao
import com.orange.androidskeleton.vo.Resource
import com.orange.androidskeleton.vo.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val webService: WebService
) {

    fun loadUser(login: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByLogin(login)

            override fun createCall() = webService.getUser(login)
        }.asLiveData()
    }
}
