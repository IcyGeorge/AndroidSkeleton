/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orange.androidskeleton.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.R
import com.orange.androidskeleton.repository.api.ApiEmptyResponse
import com.orange.androidskeleton.repository.api.ApiErrorResponse
import com.orange.androidskeleton.repository.api.ApiResponse
import com.orange.androidskeleton.repository.api.ApiSuccessResponse
import com.orange.androidskeleton.vo.Resource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.Exceptions
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>(context: Context) {

    private val result: Flowable<Resource<ResultType>>

    init {
        // Lazy disk observable.
        val diskObservable = Flowable.defer {
            loadFromDb()
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
        }

        // Lazy network observable.
        val networkObservable = Flowable.defer {
            createCall()
                // Request API on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(Schedulers.computation())
                .doOnNext { request: ApiResponse<RequestType> ->
                    if (request is ApiSuccessResponse) {
                        saveCallResult(processResponse(request))
                    }
                }
                .flatMap { loadFromDb() }
        }

        result = when {
            context.isNetworkStatusAvailable() -> networkObservable
                .map<Resource<ResultType>> { Resource.success(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { Resource.error(it) }
                .startWith(Resource.loading(null))
            else -> diskObservable
                .map<Resource<ResultType>> { Resource.success(it) }
                .onErrorReturn { Resource.error(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.loading(null))
        }
    }

    private fun Context.isNetworkStatusAvailable(): Boolean {
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
//        return connectivityManager?.activeNetworkInfo?.isConnected ?:
        return true
    }

    protected open fun onFetchFailed() {}

    fun asFlowable() = result

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flowable<ResultType>

    @MainThread
    protected abstract fun createCall(): Flowable<ApiResponse<RequestType>>
}