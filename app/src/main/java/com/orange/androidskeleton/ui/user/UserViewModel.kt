package com.orange.androidskeleton.ui.user

import androidx.lifecycle.*
import com.orange.androidskeleton.base.BaseViewModel
import com.orange.androidskeleton.repository.UserRepository
import com.orange.androidskeleton.util.AbsentLiveData
import com.orange.androidskeleton.vo.Contributor
import com.orange.androidskeleton.vo.Resource
import com.orange.androidskeleton.vo.User
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserViewModel
@Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {
    val users: LiveData<Resource<List<User>>> =
        LiveDataReactiveStreams.fromPublisher(userRepository.loadUsers())

}
