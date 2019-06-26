package com.orange.androidskeleton.util

import com.orange.androidskeleton.repository.api.ApiResponse
import io.reactivex.Flowable
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RxCallAdapterFactory : Factory() {

    private val original = RxJava2CallAdapterFactory.create()

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flowable::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java) {
            throw IllegalArgumentException("type must be a resource")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        return RxCallAdapter(original.get(getRawType(Response::class.java), annotations, retrofit) ?: return null)
    }
}
