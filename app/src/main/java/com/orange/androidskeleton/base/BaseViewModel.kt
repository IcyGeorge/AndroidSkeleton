package com.orange.androidskeleton.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel () {
    val disposable: CompositeDisposable = CompositeDisposable()
}