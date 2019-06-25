package com.orange.androidskeleton.vo


import com.orange.androidskeleton.vo.Status.ERROR
import com.orange.androidskeleton.vo.Status.LOADING
import com.orange.androidskeleton.vo.Status.SUCCESS

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val throwable: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(throwable: Throwable): Resource<T> {
            return Resource(ERROR, null, throwable)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}
