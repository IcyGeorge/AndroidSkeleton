package com.orange.androidskeleton.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.orange.androidskeleton.AppExecutors
import com.orange.androidskeleton.di.Injectable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by georgenaiem on 4/12/18.
 */

abstract class BaseFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    protected fun showMessage(content: String?) {
        Observable.just("v").observeOn(Schedulers.from(appExecutors.diskIO()))
        if (content.isNullOrEmpty())
            return
        MaterialDialog(context!!).show {
            message(text = content)
            positiveButton(android.R.string.ok)
            lifecycleOwner(owner = this@BaseFragment)
        }
    }

    protected fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

}

