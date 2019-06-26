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
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.orange.androidskeleton.repository.api.ApiEmptyResponse
import com.orange.androidskeleton.repository.api.ApiErrorResponse
import com.orange.androidskeleton.repository.api.ApiResponse
import com.orange.androidskeleton.repository.api.ApiSuccessResponse
import com.orange.androidskeleton.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

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
        result = Flowable.defer {
            loadFromDb()
                .subscribeOn(Schedulers.io())
                .flatMap { data ->
                    if (shouldFetch(data)) {
                        fetchFromNetwork()
                            .startWith(Flowable.just(Resource.success(data)))
                    } else {
                        Flowable.just(Resource.success(data))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.loading(null))

        }
    }

    private fun fetchFromNetwork() : Flowable<Resource<ResultType>> {
        return Flowable.defer {
            createCall()
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { ApiResponse.create(it) }
                .onErrorReturn { ApiResponse.create(it) }
                .flatMap { response ->
                    when (response) {
                        is ApiSuccessResponse -> {
                            saveCallResult(processResponse(response))
                            // a new observable to load updated data
                            loadFromDb().map { data -> Resource.success(data) }
                        }
                        is ApiEmptyResponse -> {
                            // reload from disk whatever we had
                            loadFromDb().map { data -> Resource.success(data) }
                        }
                        is ApiErrorResponse -> {
                            onFetchFailed()
                            loadFromDb().map { data -> Resource.error(response.throwable, data) }
                        }
                    }
                }
        }

    }

    private fun Context.isNetworkStatusAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected ?: false
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
    protected abstract fun createCall(): Single<Response<RequestType>>
}