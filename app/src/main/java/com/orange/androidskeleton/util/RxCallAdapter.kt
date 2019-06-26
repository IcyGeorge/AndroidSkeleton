package com.orange.androidskeleton.util


import com.orange.androidskeleton.repository.api.ApiResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import java.lang.reflect.Type

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
</R> */
class RxCallAdapter<R>(private val wrapped: CallAdapter<R, *>) :
    CallAdapter<R, Any> {

    override fun responseType(): Type = wrapped.responseType()

    override fun adapt(call: Call<R>): Any {
        return when (val result = wrapped.adapt(call)) {
            is Flowable<*> ->
                result
                .map { response -> ApiResponse.create(response as Response<*> ) }
                .onErrorReturn { throwable -> ApiResponse.create(throwable) }
           else -> result
        }

    }
}
